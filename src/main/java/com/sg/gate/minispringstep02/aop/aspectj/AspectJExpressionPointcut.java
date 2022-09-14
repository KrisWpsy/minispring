package com.sg.gate.minispringstep02.aop.aspectj;

import com.sg.gate.minispringstep02.aop.ClassFilter;
import com.sg.gate.minispringstep02.aop.MethodMatcher;
import com.sg.gate.minispringstep02.aop.Pointcut;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * AspectJExpressionPointcut 的核心功能主要依赖于 aspectj 组件并处理 Pointcut、
 * ClassFilter,、MethodMatcher 接口实现，专门用于处理类和方法的匹配过滤操作。
 *
 * 切点表达式实现了 Pointcut、ClassFilter、MethodMatcher，三个接口定义方法，
 * 同时这个类主要是对 aspectj 包提供的表达式校验方法使用。
 */
public class AspectJExpressionPointcut implements Pointcut, ClassFilter, MethodMatcher {

    private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<PointcutPrimitive>();

    static {
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
    }

    private final PointcutExpression pointcutExpression;

    public AspectJExpressionPointcut(String expression) {
        PointcutParser pointcutParser =  PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(SUPPORTED_PRIMITIVES, this.getClass().getClassLoader());
        pointcutExpression = pointcutParser.parsePointcutExpression(expression);
    }

    @Override
    public boolean matches(Class<?> clazz) {
        return pointcutExpression.couldMatchJoinPointsInType(clazz);
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        /*if (matches(targetClass)) {
            Method[] declaredMethods = targetClass.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                if (declaredMethod.getName().equals(method.getName())
                        && declaredMethod.getParameterCount() == method.getParameterCount())
                    return true;
            }
        }
        return false;*/
        return pointcutExpression.matchesMethodExecution(method).alwaysMatches();
    }

    @Override
    public ClassFilter getClassFilter() {
        return this;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }
}
