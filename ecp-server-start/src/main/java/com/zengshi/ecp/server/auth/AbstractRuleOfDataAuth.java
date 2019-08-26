package com.zengshi.ecp.server.auth;

import com.zengshi.ecp.frame.utils.EcpServicesUtil;
import com.zengshi.ecp.server.front.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**数据权限规则抽象类
 */
public abstract class AbstractRuleOfDataAuth implements IRuleOfDataAuth{

    private static final Logger logger=Logger.getLogger(AbstractRuleOfDataAuth.class);

    public static ThreadLocal<Object> varObject=new ThreadLocal<Object>();
    /**
     * 获取指定用户下指定功能的规则(逻辑表达式)
     * @param funcCode 功能编码
     * @param staffId 用户ID
     * @return
     */
    @Override
    public List<String> getRule(String funcCode, long staffId) {
        List<RuleObject> ruleObjects=getRuleObjects(funcCode,staffId);
        String rule=wrapRule(ruleObjects);
        List<String> rules=new ArrayList<String>();
        rules.add(rule);
        return rules;
    }

    public static String wrapRule(List<RuleObject> ruleObjects){
        if(CollectionUtils.isEmpty(ruleObjects)){
            return null;
        }
        StringBuffer sb=new StringBuffer(128);
        int i=1;
        int size=ruleObjects.size();
        for (RuleObject ro : ruleObjects){
            if(ro.isHasLeft()){
                sb.append("(");
            }

//            sb.append(ro.getName());
            Object value=ro.getValue();
            switch (ro.getOp()){
                case IsNull:
                    sb.append(ro.getName()).append("== null ");
                    break;
                case IsNotNull:
                    sb.append(ro.getName()).append("!= null ");
                    break;
                case EqualTo:
                    if(String.class.isAssignableFrom(value.getClass())){
                        sb.append(ro.getName()).append(".equals(").append(ro.getName()).append(i).append(")");
                    }else{
                        sb.append(ro.getName()).append(" == ").append(ro.getName()).append(i);
                    }
                    break;
                case NotEqualTo:
                    if(String.class.isAssignableFrom(value.getClass())){
                        sb.append(" !").append(ro.getName()).append(".equals(").append(ro.getName()).append(i).append(")");
                    }else{
                        sb.append(ro.getName()).append(" != ").append(ro.getName()).append(i);
                    }
                    break;
                case GreaterThan:
                    if(Date.class.isAssignableFrom(value.getClass())){
                        sb.append(ro.getName()).append(".after(").append(ro.getName()).append(i).append(")");
                    }else{
                        sb.append(ro.getName()).append(" > ").append(ro.getName()).append(i);
                    }
                    break;
                case GreaterThanOrEqualTo:
                    if(Date.class.isAssignableFrom(value.getClass())){
                        sb.append(" !").append(ro.getName()).append(".before(").append(ro.getName()).append(i).append(")");
                    }else{
                        sb.append(ro.getName()).append(" >= ").append(ro.getName()).append(i);
                    }
                    break;
                case LessThan:
                    if(Date.class.isAssignableFrom(value.getClass())){
                        sb.append(ro.getName()).append(".before(").append(ro.getName()).append(i).append(")");
                    }else{
                        sb.append(ro.getName()).append(" < ").append(ro.getName()).append(i);
                    }
                    break;
                case LessThanOrEqualTo: {
                    if (Date.class.isAssignableFrom(value.getClass())) {
                        sb.append(" !").append(ro.getName()).append(".after(").append(ro.getName()).append(i).append(") ");
                    } else {
                        sb.append(ro.getName()).append(" <= ").append(ro.getName()).append(i);
                    }
                    break;
                }
                case In:
                    sb.append(ro.getName()).append(i).append(".containsAll(").append(ro.getName()).append(") ");
                    break;
                case NotIn:
                    sb.append(" !").append(ro.getName()).append(i).append(".containsAll(").append(ro.getName()).append(") ");
                    break;
                case Between: {
                    Object[] values = (Object[]) value;
                    sb.append(" (").append(ro.getName()).append(">").append(values[0]).append(" && ").append(ro.getName()).append("<").append(values[1]).append(") ");
                    break;
                }
                case NotBetween: {
                    Object[] values = (Object[]) value;
                    sb.append(" !(").append(ro.getName()).append(">").append(values[0]).append(" && ").append(ro.getName()).append("<").append(values[1]).append(") ");
                    break;
                }
                case Like:{
                    String str=(String)value;
                    if(str.startsWith("%") || str.startsWith("_")){
                        sb.append(ro.getName()).append(".endsWith(\"").append(str.replaceFirst("^[%,_]", "")).append("\") ");
                    }else if(str.endsWith("%") || str.endsWith("_")){
                        sb.append(ro.getName()).append(".startsWith(\"").append(str.replaceFirst("[%,_]$","")).append("\") ");
                    }else{
                        sb.append(ro.getName()).append(".lastIndexOf(").append(ro.getName()).append(i).append(")>0 ");
                    }
                    break;
                }
                case NotLike:{
                    String str=(String)value;
                    if(str.startsWith("%") || str.startsWith("_")){
                        sb.append(" !").append(ro.getName()).append(".endsWith(\"").append(str.replaceFirst("^[%,_]", "")).append("\") ");
                    }else if(str.endsWith("%") || str.endsWith("_")){
                        sb.append(" !").append(ro.getName()).append(".startsWith(\"").append(str.replaceFirst("[%,_]$","")).append("\") ");
                    }else{
                        sb.append(" !").append(ro.getName()).append(".lastIndexOf(").append(ro.getName()).append(i).append(")>0 ");
                    }
                }
                default:
                    sb.append(ro.getName()).append(ro.getOp()).append(ro.getName()).append(i);
                    break;
            }
            if(ro.isHasRight()){
                sb.append(")");
            }
            if(i<size) {
                if (ro.isOr()) {
                    sb.append(" || ");
                } else {
                    sb.append(" && ");
                }
            }
            i++;
        }

        return sb.toString();
    }
    /**
     *获取指定用户下指定功能的规则(属性、值)
     * @param funcCode 功能编码
     * @param staffId 用户ID
     * @return key :属性名  value: 属性值
     */
    @Override
    public Map<String, Object> getRuleProperties(String funcCode, long staffId){
        List<RuleObject> ruleObjects=getRuleObjects(funcCode,staffId);
        if(CollectionUtils.isEmpty(ruleObjects)){
            return null;
        }
        Map<String,Object> map=new HashMap<String,Object>();
        for(RuleObject ro : ruleObjects){
            map.put(ro.getName(),ro.getValue());
        }
        return map;
    }
    /**
     *获取指定用户下指定功能的规则(sql代码段)
     * @param funcCode 功能编码
     * @param staffId 用户ID
     * @return
     */
    @Override
    public String getFragmentOfSql(String funcCode, long staffId) {
        List<RuleObject> ruleObjects=getRuleObjects(funcCode,staffId);

        return wrapFragmentOfSql(ruleObjects);
    }
    /**
     * 获取指定用户目录下指定功能的规则(sql代码段)mybatis格式及入参值
     * @param funcCode 功能编码
     * @param staffId 用户ID
     * @param sqlAlias sql代码段别名
     * @return
     */
    @Override
    public Map<String, Object> getFramgmentOfSql(String funcCode, long staffId, String sqlAlias) {
        List<RuleObject> ruleObjects=getRuleObjects(funcCode,staffId);

        return wrapFragmentOfMyBatisSql(ruleObjects,sqlAlias);
    }

    /**
     * 封装mybatis格式的sql语句
     * @param ruleObjects
     * @return
     */
    public static Map<String,Object> wrapFragmentOfMyBatisSql(List<RuleObject> ruleObjects,String sqlAlias){
        if(CollectionUtils.isEmpty(ruleObjects)){
            return null;
        }
        Map<String,Object> paramMap=new HashMap<String,Object>();
        StringBuffer sb=new StringBuffer(128);
        int i=1;
        int size=ruleObjects.size();
        for(RuleObject ro : ruleObjects){
            if(ro.isHasLeft()){
                sb.append("(");
            }
            sb.append(ro.getField());
            sb.append(" ").append(ro.getOp().getOperate()).append(" ");
            switch (ro.getOp()){
                case Between:
                case NotBetween:
                    Object[] values=(Object[])ro.getValue();
                    sb.append(" #{").append(ro.getField()).append(i).append("a").append("} and")
                            .append(" #{").append(ro.getField()).append(i).append("b").append("}");
                    paramMap.put(ro.getField()+i+"a",values[0]);
                    paramMap.put(ro.getField()+i+"b",values[1]);
                    break;
                case In:
                case NotIn:
                    List<?> list=(List<?>)ro.getValue();
                    sb.append("(");
                    StringBuffer sf=new StringBuffer(128);
                    int k=1;
                    for(Object value : list){
                        sf.append("#{").append(ro.getField()).append("_").append(k).append("},");
                        paramMap.put(ro.getField()+"_"+k,value);
                        k++;
                    }
                    sb.append(sf.toString().replaceFirst(",$", "")).append(") ");
                    break;
                default:
                    sb.append(" #{").append(ro.getField()).append(i).append("} ");
                    paramMap.put(ro.getField()+i,ro.getValue());
            }
            if(ro.isHasRight()){
                sb.append(")");
            }
            if(i<size) {
                if (ro.isOr()) {
                    sb.append(" or ");
                } else {
                    sb.append(" and ");
                }
            }
            i++;
        }
        if(org.springframework.util.StringUtils.hasText(sqlAlias)){
            paramMap.put(sqlAlias,sb.toString());
        }else{
            paramMap.put("sql",sb.toString());
        }
        if(logger.isDebugEnabled()){
            logger.debug("=============================sql语句："+sb.toString());
        }
        return paramMap;
    }

    /**
     * 封装sql代码片段
     * @param ruleObjects
     * @return
     */
    public static String wrapFragmentOfSql(List<RuleObject> ruleObjects){
        if(CollectionUtils.isEmpty(ruleObjects)){
            return null;
        }

        StringBuffer sb=new StringBuffer(128);
        int i=1;
        int size=ruleObjects.size();
        for(RuleObject ro : ruleObjects){
            if(ro.isHasLeft()){
                sb.append("(");
            }
            sb.append(ro.getField());
            sb.append(" ").append(ro.getOp().getOperate()).append(" ");
            switch (ro.getOp()){
                case Between:
                case NotBetween:
                    Object[] values=(Object[])ro.getValue();
                    sb.append(" '").append(values[0]).append("' and").append(" '").append(values[1]).append("' ");
                    break;
                case In:
                case NotIn:
                    List<?> list=(List<?>)ro.getValue();
                    sb.append("(");
                    StringBuffer sf=new StringBuffer(128);
                    for(Object value : list){
                        sf.append("'").append(value).append("',");
                    }
                    sb.append(sf.toString().replaceFirst(",$", "")).append(") ");
                    break;
                default:
                    sb.append(" '").append(ro.getValue()).append("' ");

            }
            if(ro.isHasRight()){
                sb.append(")");
            }
            if(i<size) {
                if (ro.isOr()) {
                    sb.append(" or ");
                } else {
                    sb.append(" and ");
                }
            }
            i++;
        }
        if(logger.isDebugEnabled()){
            logger.debug("=============================sql语句："+sb.toString());
        }
        return sb.toString();
    }
    /**
     *获取指定用户下指定功能的规则项目集合
     * @param funcCode 功能编码
     * @param staffId 用户ID
     * @return com.zengshi.ecp.server.auth.RuleObject类对象
     */
    @Override
    public abstract List<RuleObject> getRuleObjects(String funcCode, long staffId);
    /**
     * 判断指定用户是否配置指定功能的数据权限
     * @param funcCode
     * @return
     */
    @Override
    public abstract boolean judgeAuthOfCurrentUser(String funcCode, long staffId);

    /**
     *多个规则合并
     * 目前只是简单取各个规则的逻辑或，有待优化
     * @param ruleObjects 多个规则
     * @return
     */
    public static List<RuleObject> addRuleObjects(List<List<RuleObject>> ruleObjects){
        List<RuleObject> newRos=new ArrayList<RuleObject>();
        for(List<RuleObject> ros : ruleObjects){
            if(CollectionUtils.isEmpty(ros)){
                continue;
            }
            int size=newRos.size();
            if(size>0){
                RuleObject ro=newRos.get(size-1);
                ro.setIsOr(true);//修改最后一个规则项目的逻辑符为or
            }
            newRos.addAll(ros);
        }

        return newRos;
    }

    /**
     * 解析规则项目值的变量
     * @param ruleObjects 规则项目集合
     * @param object 变量取值对象
     * @return
     */
    public static void analysisVariable(List<RuleObject> ruleObjects,Object object){
        if(null==object){
            object=varObject.get();
        }
        if(null==object){
            logger.warn("================ 变量对象为null。");
            return;
        }
        for(RuleObject ro : ruleObjects){
            Object obj=ro.getValue();
            if(null==obj || !String.class.isAssignableFrom(obj.getClass())){
                continue;
            }
            String value=(String)obj;
//            String reg="^\\$\\{(\\w+)\\.?(\\w*)\\}$";
            String reg="^#\\{(\\w+\\.?\\w*)\\}$";
            Pattern pattern=Pattern.compile(reg);
            Matcher matcher=pattern.matcher(value);
            String varName=null;
            boolean flag=false;
            Object varValue=object;
            if(matcher.find()){
                varName=matcher.group(1);
                if(object.getClass().getName().startsWith(DataAuthHandler.DEFAULT_PACKAGE)){
                    varValue=getRealValue(object, varName);
                }else if(Map.class.isAssignableFrom(object.getClass())){
                    varValue=((Map)object).get(varName);
                }
                flag=true;
            }else{
//                String mreg="^@\\{(\\w+)\\.?(\\w*)\\}$";
                String mreg="^@\\{(\\w+)\\.?(\\w+\\(.*\\))\\}$";
                Pattern mpattern=Pattern.compile(mreg);
                Matcher mmatcher=mpattern.matcher(value);
                String pname=null;
                if(mmatcher.find()){
                    varName=mmatcher.group(1);
                    pname=mmatcher.group(2);
                    Object varObj= EcpServicesUtil.getBean(varName);
                    varValue=getRealValue(varObj, pname);
                }
                flag=matcher.groupCount()==3?true:false;
            }
            if(!flag){
                continue;
            }
            ro.setValue(varValue);
        }
    }

    public static Object getRealValue(Object varObject,String expression){
        ExpressionParser parser=new SpelExpressionParser();
        Expression exp=parser.parseExpression(expression);
        EvaluationContext context=new StandardEvaluationContext(varObject);
        return exp.getValue(context);
    }

    /**
     * 设置动态参数变量值对象
     * @param object
     */
    public static void setVarObject(Object object){
        varObject.set(object);
    }

    /**
     * 值类型转换
     * @param ruleObject
     * @param valueClazz
     */
    public static void convertValueClass(RuleObject ruleObject,String valueClazz){
        if(StringUtils.isBlank(valueClazz) || ruleObject==null){
            return;
        }
        Object value=ruleObject.getValue();
        if(null==value){
            return;
        }
        if(List.class.isAssignableFrom(value.getClass())){
            List values=(List)value;
            List nvalues=new ArrayList();
            for(Object val : values){
                Object nval= convertValue(val,valueClazz);
                nvalues.add(nval);
            }
            ruleObject.setValue(nvalues);
        }else if(value.getClass().isArray()){
            Object[] values=(Object[])value;
            Object[] nvalues=new Object[values.length];
            for(int i=0;i<values.length;i++){
                Object nval=convertValue(values[i],valueClazz);
                nvalues[i]=nval;
            }
            ruleObject.setValue(nvalues);
        }else{
            value=convertValue(value,valueClazz);
            ruleObject.setValue(value);
        }

    }

    public static Object convertValue(Object value,String valueClazz){
        Object newValue=null;
        switch (valueClazz){
            case "int":{
                newValue=Integer.valueOf(value.toString()).intValue();
                break;
            }
            case "Integer":
            case "java.lang.Integer":
                newValue=Integer.valueOf(value.toString());
                break;
            case "double":
                newValue=Double.valueOf(value.toString()).doubleValue();
                break;
            case "Double":
            case "java.lang.Double":
                newValue=Double.valueOf(value.toString());
                break;
            case "float":
                newValue=Float.valueOf(value.toString()).floatValue();
                break;
            case "Float":
            case "java.lang.Float":
                newValue=Float.valueOf(value.toString());
                break;
            case "long":
                newValue=Long.valueOf(value.toString()).longValue();
                break;
            case "Long":
            case "java.lang.Long":
                newValue=Long.valueOf(value.toString());
                break;
            case "short":
                newValue=Short.valueOf(value.toString()).shortValue();
                break;
            case "Short":
            case "java.lang.Short":
                newValue=Short.valueOf(value.toString());
                break;
            case "boolean":
                newValue=Boolean.valueOf(value.toString()).booleanValue();
                break;
            case "Boolean":
            case "java.lang.Boolean":
                newValue=Boolean.valueOf(value.toString());
                break;
            case "java.util.Date":
                try {
                    newValue=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value.toString());
                } catch (ParseException e) {
                    throw new BusinessException("日期转换失败，要求字符串的格式为：yyyy-MM-dd HH:mm:ss");
                }
                break;
            default:
                newValue=value;
                break;
        }

        return newValue;
    }

    public static void main(String[] args){
        String[] arr={"1","2"};
        RuleObject ruleObject=new RuleObject("field",">=", "false");
        convertValueClass(ruleObject,"boolean");
        System.out.println(ruleObject.getValue()+"   "+ruleObject.getValue().getClass().getName());
    }
}
