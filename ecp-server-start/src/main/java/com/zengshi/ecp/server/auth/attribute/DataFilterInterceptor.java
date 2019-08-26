package com.zengshi.ecp.server.auth.attribute;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 */
public class DataFilterInterceptor {

    private final static Logger logger=Logger.getLogger(DataFilterInterceptor.class);

    public void afterReturn(JoinPoint jp,Object returnValue){

        if(null==returnValue  || returnValue.getClass().isPrimitive()){
            return;
        }

        if(!RuleOfDataFilterUtil.isExistOfRuleFilter()){
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

        DataFilter dataFilter=method.getAnnotation(DataFilter.class);
        if(null==dataFilter){
            return;
        }
        String funcCode=dataFilter.funcCode();
        DataFormatter[] fmts=dataFilter.formatters();
        if(!StringUtils.hasText(funcCode)){
            logger.warn("********************************功能编码为空。");
            return;
        }

        List<FilterRule> rules=RuleOfDataFilterUtil.getRules(funcCode);
        if(CollectionUtils.isEmpty(rules)){
            logger.warn("********************************没有找到过滤规则。");
            return;
        }


        filterDatas(returnValue,rules,fmts);
    }

    protected void filterDatas(Object returnValue,List<FilterRule> rules,DataFormatter[] fmts){
        if (Collection.class.isAssignableFrom(returnValue.getClass())) {
            Collection col=(Collection)returnValue;
            for(Iterator itr=col.iterator();itr.hasNext();){
                Object item=itr.next();
                filterDatas(item,rules,fmts);
            }
        }else if(returnValue.getClass().isArray()){
            Object[] arr=(Object[])returnValue;
            int len=arr.length;
            for(int i=0;i<len;i++){
                Object item=arr[i];
                filterDatas(item,rules,fmts);
            }
        }else{//TODO Map类型、基本类型测试
            filterData(returnValue,rules,fmts);
        }
    }

    protected void filterData(Object returnValue,List<FilterRule> rules,DataFormatter[] fmts){
        for(FilterRule rule : rules){
            String clazz=rule.getClazz();
            String name=rule.getName();
            String fmt=rule.getFormatter();
            String value=rule.getValue();

            Object nvalue=null;
            if("java.util.Date".equals(clazz)){
                try {
                    nvalue=new SimpleDateFormat(fmt).parse(value);
                } catch (ParseException e) {
                    logger.error("日期转换失败("+value+")",e);
                }
            }else{
                if(!StringUtils.hasText(clazz) || "java.lang.String".equals(clazz) || "String".equals(clazz)){
                    value="'"+value+"'";
                }
                ExpressionParser parser=new SpelExpressionParser();
                try {
                    nvalue=parser.parseExpression(value).getValue(Class.forName(clazz));
                } catch (ClassNotFoundException e) {
                    logger.error("数值类型转换失败！",e);
                }
            }

            setValue(returnValue,name,nvalue);
            //1、value：为null处理  2、结果集类型：数组、集合、Map、对象、基本类型、基本类型数组

        }
    }


    public static Object getValue(Object varObject,String expression){
        if(Map.class.isAssignableFrom(varObject.getClass())){
            Map map=(Map)varObject;
            return map.get(expression);
        }
        ExpressionParser parser=new SpelExpressionParser();
        Expression exp=parser.parseExpression(expression);
        EvaluationContext context=new StandardEvaluationContext(varObject);
        return exp.getValue(context);
    }

    public static void setValue(Object varObject,String expression,Object valueObject){
        if(Map.class.isAssignableFrom(varObject.getClass())){
            Map map=(Map)varObject;
            map.put(expression,valueObject);
            return;
        }
        ExpressionParser parser=new SpelExpressionParser();
        Expression exp=parser.parseExpression(expression);
        EvaluationContext context=new StandardEvaluationContext(varObject);
        exp.setValue(context,valueObject);
    }

}
