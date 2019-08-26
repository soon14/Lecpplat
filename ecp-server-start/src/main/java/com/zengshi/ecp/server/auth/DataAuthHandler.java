package com.zengshi.ecp.server.auth;

import bsh.Interpreter;

import com.zengshi.ecp.frame.vo.BaseCriteria;
import com.zengshi.ecp.server.front.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**数据权限规则处理类
 */
public class DataAuthHandler {
    private static final Logger logger=Logger.getLogger(DataAuthHandler.class);
    public static final String DEFAULT_PACKAGE = "com.zengshi.ecp";

    /**
     * 执行数据权限规则逻辑表达式
     * @param expression 规则表达式
     * @param dataObject 数据对象
     * @param props 规则属性、值，属性名在表达式中是变量名
     * @return
     * @throws Exception
     */
    public static boolean handleLogicExp(String expression,Object dataObject,Map<String,Object> props,List<RuleObject> ruleObjects) throws Exception {
        StringBuffer sb=new StringBuffer(128);
        List<String> imports=new ArrayList<String>();
        Interpreter interpreter = new Interpreter();
        for(String propName:props.keySet()){
            if(dataObject.getClass().getName().startsWith(DEFAULT_PACKAGE) || Map.class.isAssignableFrom(dataObject.getClass())){
                Object value=getValueOfProperty(propName.trim(),dataObject);
                interpreter.set(propName,value);
                if(null!=value && !imports.contains(value.getClass().getName())){
                    imports.add(value.getClass().getName());
                }

            }else{
                interpreter.set(propName,dataObject);
                if(null!=dataObject && !imports.contains(dataObject.getClass().getName())){
                    imports.add(dataObject.getClass().getName());
                }
            }
        }
        int i=1;
        for(RuleObject ro : ruleObjects){
            interpreter.set(ro.getName()+i,ro.getValue());
            i++;
        }
        for(String imp : imports){
            sb.append("import ").append(imp).append(";\n");
        }
        String exp="flag=("+expression+");";
        sb.append(exp);
        if(logger.isDebugEnabled()){
            logger.debug("规则表达式："+sb.toString());
        }
        interpreter.eval(sb.toString());
        Object result=interpreter.get("flag");
        return null==result?false:(Boolean)result;
    }

    /**
     * 执行数据权限查询规则
     * @param dataObject 数据对象
     * @param props 规则属性、值，属性名在表达式中是变量名
     * @param includes 包含的规则属性
     * @param excludes 排除的规则属性
     */
    public static void handleQueryRule(Object dataObject,Map<String,Object> props,String[] includes,String[] excludes) throws Exception{
        if(!dataObject.getClass().getName().startsWith(DEFAULT_PACKAGE)){
            throw new BusinessException("数据对象要求是自定类的实例。");
        }
        if(null!=includes && includes.length>0){
            for(String include : includes){
                Object value=props.get(include);
                if(null!=value){
                    setValueOfProperty(include,value,dataObject);
                }
            }
            return;
        }
        for(String propName : props.keySet()){
            boolean flag=true;
            for(String exclude : excludes){
                if(propName.equals(exclude)){
                    flag=false;
                    break;
                }
            }
            if(flag){
                Object value=props.get(propName);
                setValueOfProperty(propName,value,dataObject);
            }else{
                continue;
            }
        }
    }

    /**
     *执行数据权限查询规则
     * @param dataObject 数据对象
     * @param ruleObjects 规则对象集合
     * @param includes 执行规则项目
     * @param excludes 排除执行规则项目
     * @throws Exception
     */
    public static void handleQueryRule(Object dataObject,List<RuleObject> ruleObjects,String[] includes,String[] excludes) throws Exception{
        Set<String> imports=new HashSet<>();
        Interpreter interpreter = new Interpreter();
        interpreter.set("dataObject",dataObject);
        if(BaseCriteria.class.isAssignableFrom(dataObject.getClass())){
            imports.add(dataObject.getClass().getName());
//            imports.add(List.class.getName());
            StringBuffer sb=new StringBuffer(1024);
            boolean first=true;
            int index=0;
            for(RuleObject ruleObject : ruleObjects){
                if(null!=ruleObject.getValue() && !imports.contains(ruleObject.getValue().getClass().getName())){
                    imports.add(ruleObject.getValue().getClass().getName().replaceFirst(";$", ""));
                }
                boolean flag=true;
                for(String in : includes){//设置包含的属性条件
                    if(!ruleObject.getName().equals(in)){
                        flag=false;
                        break;
                    }
                }
                for(String ex : excludes){//排除属性条件
                    if(ruleObject.getName().equals(ex)){
                        flag=false;
                        break;
                    }
                }

                if(!flag){
                    continue;
                }
                if(first){//TODO criteria数组优化  规则值传入优化(方法传入值)
                    String criteria=dataObject.getClass().getSimpleName()+".Criteria";
//                    sb.append("List<").append(criteria).append("> criterias=dataObject.getOredCriteria();\n")
                    sb.append("criterias=dataObject.getOredCriteria();\n")
                            .append(criteria).append(" criteria=null;\n")
                            .append("if(null!=criterias && criterias.size()>0){\n")
                            .append("criteria=criterias.get(criterias.size()-1);\n")
                            .append("}else{\n")
                            .append("criteria=dataObject.or();\n")
                            .append(" }\n")
                            .append("criteria");
//                    sb.append("dataObject.or()");
                }
                wrapCriteriaMethod(ruleObject,sb,interpreter,index++);
                first=false;
                if(ruleObject.isOr()){
                    sb.append(";\n");
                    sb.append("dataObject.or()");
                }
            }
            StringBuffer sf=new StringBuffer(128);
            for(String imp : imports){
                imp=imp.replaceFirst("^\\[L","");
                imp=imp.replaceFirst(";$", "");
                if(!imp.startsWith("java.lang.")){
                    sf.append("import ").append(imp).append(";\n");
                }

            }
            sf.append(sb).append(";");
            if(logger.isDebugEnabled()){
                logger.debug("执行表达式："+sf.toString());
            }
            interpreter.eval(sf.toString());
        }
    }

    protected  static void wrapCriteriaMethod(RuleObject ruleObject,StringBuffer sb,Interpreter interpreter,int index) throws Exception{
        sb.append(".and")
                .append(StringUtils.capitalize(ruleObject.getName()))
                .append(ruleObject.getOp().getValue())
                .append("(");
        switch (ruleObject.getOp()){
            case IsNull:
            case IsNotNull:
                sb.append(")");
                break;
            case Between:
            case NotBetween:
                Object[] values=(Object[])ruleObject.getValue();
                sb.append(ruleObject.getName())
                        .append(index)
                        .append("a").append(",")
                        .append(ruleObject.getName())
                        .append(index)
                        .append("b)");
                interpreter.set(ruleObject.getName()+index+"a",values[0]);
                interpreter.set(ruleObject.getName()+index+"b",values[1]);
//                sb.append(values[0]).append(",").append(values[1]).append(")");
                break;
            default:
                sb.append(ruleObject.getName()).append(index).append(")");
                interpreter.set(ruleObject.getName()+index,ruleObject.getValue());
//                if(String.class.isAssignableFrom(ruleObject.getValue().getClass())){
//                    sb.append("\"").append(ruleObject.getValue()).append("\")");
//                }else{
//                    sb.append(ruleObject.getValue()).append(")");
//                }
                break;
        }
    }
    public static void setValueOfProperty(String propName,Object propValue,Object target){
        if(Map.class.isAssignableFrom(target.getClass())){
            Map<String,Object> map=(Map)target;
            map.put(propName,propValue);
            return;
        }
        try{
            PropertyDescriptor pd=new PropertyDescriptor(propName,target.getClass());
            Method setMothed=pd.getWriteMethod();
            setMothed.invoke(target,new Object[]{propValue});
        }catch (Exception e){
            logger.warn("对象属性："+propName+" 赋值失败！");
        }

    }
    public static Object getValueOfProperty(String propName,Object target){
        if(Map.class.isAssignableFrom(target.getClass())){
            Map<String,Object> map=(Map)target;
            return map.get(propName);
        }

        Object value= null;
        try {
            PropertyDescriptor pd=new PropertyDescriptor(propName,target.getClass());
            value = pd.getReadMethod().invoke(target,new Object[]{});
        } catch (Exception e) {
            logger.warn("对象不存在属性："+propName);
        }
        return value;
    }

    public static void main(String[] args){
        Test test=new  Test();
        test.setName("测试");
        test.setAge(60);
        test.setNow(new Date());
        Map<String,Object> props=new HashMap<String,Object>();
        String name="aa12";
        int age=10;
        props.put("name",name);
        props.put("age",age);
        String exp="names.contains(\""+name+"\")";//"name.startsWith(\""+name+"\")";
        List<String> list=new ArrayList<String>();
        list.add("aa");
        list.add("bb");
        props.put("names",list);
        try {
//            boolean flag=handleLogicExp(exp,list,props,null);
//            System.out.println(flag);
            String str="${name}";
            String reg="^\\$\\{(\\w+)\\.?(\\w*)\\}$";
            Pattern pattern=Pattern.compile(reg);
            Matcher matcher=pattern.matcher(str);
//            matcher.matches();
//            matcher.find();
//            matcher.group();
            System.out.println(matcher.find());
            System.out.println(matcher.groupCount());
            System.out.println(int.class.getName());
//            System.out.println("[Ljava.lang.Object".replaceFirst("^\\[L",""));
//            handleQueryRule(test,props,new String[]{},new String[]{"age"});
//            System.out.println("name: "+test.getName()+ " age:"+test.getAge() + "  now: "+test.getNow());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Test{
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getNow() {
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    private Date now;
    private int age;
    private String name;
}
