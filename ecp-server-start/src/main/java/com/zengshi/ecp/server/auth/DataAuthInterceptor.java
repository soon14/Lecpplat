package com.zengshi.ecp.server.auth;

import com.zengshi.ecp.frame.vo.BaseCriteria;
import com.zengshi.ecp.server.front.dto.BaseInfo;
import com.zengshi.ecp.server.front.exception.BusinessException;
import com.zengshi.paas.common.ICriteria;
import com.zengshi.paas.utils.MethodParamNameUtil;
import com.zengshi.paas.utils.StringUtil;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**数据权限拦截器
 */
public class DataAuthInterceptor {

    private static final Logger logger=Logger.getLogger(DataAuthInterceptor.class);
    //用户没分配权限处理，true：不处理 false：异常
    private boolean noAuthOfUser;

    public void setNoAuthOfUser(boolean noAuthOfUser) {
        this.noAuthOfUser = noAuthOfUser;
    }

    public void before(JoinPoint jp){
        if(RuleOfDataAuthUtil.getRuleOfDataAuth()==null){
            if(logger.isInfoEnabled()){
                logger.warn("数据权限规则(ruleOfDataAuth)对象为空.");
            }
            return;
        }
        Method md = ((MethodSignature) jp.getSignature()).getMethod();
        Object target = jp.getTarget();
        Method method = null;
        try {
            method = target.getClass().getMethod(jp.getSignature().getName(),
                    md.getParameterTypes());
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
        List<DataAuthValid> dataAuthValidList=null;//验证规则集合
        DataAuthValid dataAuthValid=method.getAnnotation(DataAuthValid.class);
        if(null==dataAuthValid){
            DataAuthValids dataAuthValids=method.getAnnotation(DataAuthValids.class);
            dataAuthValidList=Arrays.asList(dataAuthValids.value());
        }else {
            dataAuthValidList=new ArrayList<DataAuthValid>();
            dataAuthValidList.add(dataAuthValid);
        }

        Annotation[][] annotationses = method.getParameterAnnotations();

        if (null == annotationses) {
            return;
        }
        Object[] args = jp.getArgs();
        if (null == args) {
            return;
        }
        int argSize=args.length;
        Class[] argCls=new Class[argSize];
        for(int i=0;i<argSize;i++){
            if(null==args[i]){
                argCls[i]=null;
            }else{
                argCls[i]=args[i].getClass();
            }
        }
        String[] argNames={};//数据对象变量名
        try {
            argNames=MethodParamNameUtil.getMethodParamNames(target.getClass(),method.getName(),argCls);
        } catch (Exception e) {
            logger.warn("获取方法参数名失败.类："+target.getClass()+" 方法："+method.getName() +" 方法参数："+argCls.getClass().getName());
        }
        int index = 0;
        List<Object> dataObjList=new ArrayList<Object>();//数据对象集合
        Map<String,Object> dataObjMap=new HashMap<String,Object>();//数据对象集合，key值为@DataObject的value
        List<DataObject> doList=new ArrayList<DataObject>();//数据对象注解集合
        Map<String,DataObject> doMap=new HashMap<String,DataObject>();//数据对象注解集合，key值为@DataObject的value
        Map<String,Object> varObjMap=new HashMap<String,Object>();//value变量集合，key为@VarObject的value或变量名
        for(Annotation[] annotations : annotationses){
            for(Annotation annotation : annotations){
                if(DataObject.class.isAssignableFrom(annotation.getClass())){
                    Object arg=args[index];
                    if(!BaseCriteria.class.isAssignableFrom(arg.getClass())){
                        if(logger.isInfoEnabled()){
                            logger.warn("类["+target.getClass().toString()+"]的方法["+method.getName()+"]第"+(index+1)+"参数没有实现com.zengshi.ecp.frame.vo.BaseCriteria接口.");
                        }
                    }
                    DataObject doa=(DataObject)annotation;
                    String key=doa.value();
                    if(StringUtil.isBlank(key)){
                        String argName=argNames[index];
                        dataObjMap.put(argName,arg);
                        doMap.put(argName,doa);
                    }else{
                        dataObjMap.put(key,arg);
                        doMap.put(key,doa);
                    }
                    dataObjList.add(arg);
                    doList.add(doa);
                }else if(VarObject.class.isAssignableFrom(annotation.getClass())){
                    VarObject var=(VarObject)annotation;
                    String argName=var.value();
                    Object arg=args[index];
                    if(!StringUtils.hasText(argName)){
                        argName=argNames[index];
                    }
                    varObjMap.put(argName,arg);
                }
            }
            index++;
        }
        doValid(dataAuthValidList,dataObjList,dataObjMap,doList,doMap,varObjMap);
    }

    /**
     * 判断当前用户是否具备权限
     * @return
     */
    protected boolean judgeAuthOfCurrentUser(String funcCode){

        return RuleOfDataAuthUtil.judgeAuthOfCurrentUser(funcCode);
    }

    protected void doValid(List<DataAuthValid> davList,List<Object> dataObjList,Map<String,Object> dataObjMap,List<DataObject> doList,Map<String,DataObject> doMap,Map<String,Object> varObjMap){
        if(CollectionUtils.isEmpty(davList) || (CollectionUtils.isEmpty(dataObjList) && CollectionUtils.isEmpty(dataObjMap))){
//            logger.trace("没有定义验证规则或数据对象");
//            return;
            throw new BusinessException("没有定义验证规则或数据对象");
        }
        int index=0;
        int size=davList.size();
        for(DataAuthValid dav : davList){
            String value=dav.value();
            Object dataObject=null;
            DataObject doa=null;
            if(StringUtils.hasText(value) && !CollectionUtils.isEmpty(dataObjMap)){
                String[] values=value.split(",");
                if(values.length>1){
                    Map<String,Object> map=new HashMap<String,Object>();
                    for(String val : values){
                        map.put(val,dataObjMap.get(val));
                    }
                    dataObject=map;
                }else {
                    dataObject=dataObjMap.get(value);
                    doa=doMap.get(value);
                }

            }else if(!CollectionUtils.isEmpty(dataObjList)){
                dataObject=dataObjList.get(index);
                doa=doList.get(index);
                index++;
            }
            if(null==dataObject && size>1){
                logger.warn("数据对象不存在: 功能编码 "+dav.funcCode());
                continue;
            }
            //用户没有配置数据权限处理
            if(!judgeAuthOfCurrentUser(dav.funcCode())){
                if(!dav.isIntercept()){
                    logger.trace("当前用户没有配置数据权限，不做拦截。");
                    continue;
                }else{
                    if(noAuthOfUser){
                        throw new BusinessException("没有功能["+dav.funcCode()+"]的数据处理权限.");
                    }
                }
            }
           //查询验证
            Map<String,Object> props=RuleOfDataAuthUtil.getRuleProperties(dav.funcCode());
            if(DataAuthType.BIND.equals(dav.authType())){
                try {
                    if(BaseCriteria.class!=dataObject.getClass() && BaseCriteria.class.isAssignableFrom(dataObject.getClass())){
                        List<RuleObject> ruleObjects=resolveRuleValueExp(dav.funcCode(),varObjMap);;//RuleOfDataAuthUtil.getRuleObjects(dav.funcCode());
                        DataAuthHandler.handleQueryRule(dataObject,ruleObjects,doa.include(),doa.exclude());
                    }else if(BaseInfo.class!=dataObject.getClass() && BaseInfo.class.isAssignableFrom(dataObject.getClass())){
                        BaseInfo baseInfo=(BaseInfo)dataObject;
                        List<RuleObject> ruleObjects=resolveRuleValueExp(dav.funcCode(),varObjMap);//RuleOfDataAuthUtil.getRuleObjects(dav.funcCode());
                        ICriteria criteria=baseInfo.getCriteria();
                        if(null==criteria && dav.criteriaClass()!=BaseCriteria.class){
                            criteria=dav.criteriaClass().newInstance();
                            baseInfo.setCriteria(criteria);
                        }
                        DataAuthHandler.handleQueryRule(criteria,ruleObjects,doa.include(),doa.exclude());
                    }else{
                        DataAuthHandler.handleQueryRule(dataObject,props,doa.include(),doa.exclude());
                    }
                } catch (Exception e) {
                    throw new RuntimeException("查询数据权限规则执行异常",e);
                }
            }else{
                List<String> rules=RuleOfDataAuthUtil.getRule(dav.funcCode());
                List<RuleObject> ruleObjects=resolveRuleValueExp(dav.funcCode(),varObjMap);//RuleOfDataAuthUtil.getRuleObjects(dav.funcCode());
//                if(!StringUtils.hasText(rule)){
//                    throw new BusinessException("功能["+dav.funcCode()+"]没找到规则.");
//                }
                boolean flag=false;
                for(String rule : rules){
                    try {
                        flag=(flag || DataAuthHandler.handleLogicExp(rule,dataObject,props,ruleObjects));
                    } catch (Exception e) {
                        throw new RuntimeException("数据权限规则执行异常.",e);
                    }
                }
                if(!flag){
                    throw new BusinessException("您没有权限执行该操作.");
                }
            }

        }
    }

    /**
     * 解析规则项目值的变量值
     * @param funcCode
     * @param varObjMap
     * @return
     */
    private List<RuleObject> resolveRuleValueExp(String funcCode,Map<String,Object> varObjMap){
        List<RuleObject> ruleObjects=RuleOfDataAuthUtil.getRuleObjects(funcCode);
        if(CollectionUtils.isEmpty(ruleObjects)){
            return ruleObjects;
        }
        if(CollectionUtils.isEmpty(varObjMap)){
            Object varObj=AbstractRuleOfDataAuth.varObject.get();
            if(null!=varObj){
                AbstractRuleOfDataAuth.analysisVariable(ruleObjects,varObj);
            }
            return ruleObjects;
        }

        Collection values=varObjMap.values();
        if(values.size()==1){//如果只有一个变量对象，且为自定义类的实例或map的实例
            Object[] objects=values.toArray();
            Object object=objects[0];
            if(object.getClass().getName().startsWith(DataAuthHandler.DEFAULT_PACKAGE) || Map.class.isAssignableFrom(object.getClass())){
                AbstractRuleOfDataAuth.analysisVariable(ruleObjects,object);
            }else {
                AbstractRuleOfDataAuth.analysisVariable(ruleObjects,varObjMap);
            }
        }else{
            AbstractRuleOfDataAuth.analysisVariable(ruleObjects,varObjMap);
        }

        return ruleObjects;
    }
}
