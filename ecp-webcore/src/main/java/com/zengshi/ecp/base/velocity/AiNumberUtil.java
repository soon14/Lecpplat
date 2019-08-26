/** 
 * Date:2015-8-28下午8:48:40 
 * 
 */
package com.zengshi.ecp.base.velocity;

import org.apache.commons.lang.StringUtils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description: <br>
 * Date:2015-8-28下午8:48:40 <br>
 * 
 * @version
 * @since JDK 1.6
 */
public class AiNumberUtil {
    /**
     * 带小数的格式化
     */
    private static DecimalFormat doubleFormatter = new DecimalFormat("#0.00");

    /**
     * 带一位小数的格式化
     */
    private static DecimalFormat doubleFormatterByOne = new DecimalFormat("#0.0");

    /**
     * 整数的格式化
     */
    private static DecimalFormat digitsFormatter = new DecimalFormat("#0.##");

    /**
     * 对传入的金额，除以 100 并进行格式化展示； 如果最终的结果是 整数，则不展示小数部分； 如果最终的结果是 小数，则展示小数部分，并且要求为小数点后必须为2位；
     * 
     * @param account
     * @return
     */
    public String showMoney(String account) {

        // 如果传入的金额为 空，那么返回 0 ；
        if (StringUtils.isEmpty(account)) {
            return "0";
        }
        double number = 0D;

        // /如果传入的非数字，则展示为 原来的值；
        try {
            number = Double.parseDouble(account);
        } catch (Exception err) {

            return account;
        }

        // 如果是整数； 因为是 /100 的，所以根据传入的值，判断是否是 2个 0 结尾；
        if (isDoubleZeroEnd(account)) {
            return digitsFormatter.format(number / 100);
        } else {
            return doubleFormatter.format(number / 100);
        }

    }

    /**
     * 判断是否整数
     * 
     * @param number
     * @return
     */
    private boolean isDoubleZeroEnd(String number) {
        // 如果是 0 ，那么判断为整数
        if ("0".equalsIgnoreCase(number)) {
            return true;
        }
        Pattern ptn = Pattern.compile("^[1-9]{1}[0-9]*[0]{2}$");
        Matcher m = ptn.matcher(number);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 对传入的金额，除以 100 并进行格式化展示； 如果最终的结果是 整数，则添加两位小数，小数值为00； 如果最终的结果是 小数，则展示小数部分，并且要求为小数点后必须为2位；
     * 
     * @param account
     * @return
     */
    public String showMoneyByTwoDecimal(String account) {

        // 如果传入的金额为 空，那么返回 0 ；
        if (StringUtils.isEmpty(account)) {
            return "0.00";
        }

        // 如果传入的金额为0，那么返回 0.00；
        if ("0".equals(account)) {
            return "0.00";
        }

        double number = 0D;

        // /如果传入的非数字，则展示为 原来的值；
        try {
            number = Double.parseDouble(account);
        } catch (Exception err) {

            return account;
        }

        // 返回带两位小数的数据，无论是整数还是已经有小数的数据。
        return doubleFormatter.format(number / 100);
    }

    // 返回带一位小数的
    public String showPercentNum(String percentNum) {

        // 如果传入的百分比为 空，那么返回 0.0;
        if (StringUtils.isEmpty(percentNum)) {
            return "0.0";
        }

        // 如果传入的金额为0，那么返回 0.00；
        if ("0".equals(percentNum)) {
            return "0.0";
        }

        double number = 0D;

        // /如果传入的非数字，则展示为 原来的值；
        try {
            number = Double.parseDouble(percentNum);
        } catch (Exception err) {

            return percentNum;
        }

        // 返回带两位小数的数据，无论是整数还是已经有小数的数据。
        return doubleFormatterByOne.format(number);

    }
}
