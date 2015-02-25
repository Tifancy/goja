/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package goja.validator;

/**
 * <p>
 * 常用正则表达式.
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-11-30 14:02
 * @since JDK 1.6
 */
public interface RegexPool {

    /**
     * 全中文验证
     */
    String CONTAINCHINESE    = "[\\w\\W\\s]*([\u4E00-\u9FA5]|[\uFE30-\uFFA0])+[\\s\\W\\w]*";
    /**
     * 包含全角符号
     */
    String FULLSHAPED        = "[^\\x00-\\xff]";
    /**
     * 含有空格
     */
    String CONTAINSPACE      = "^.*[\\s]+.*$";
    /**
     * 含有英文
     */
    String CONTAINENGLISH    = "^.*[a-zA-Z]+.*$";
    /**
     * 含有数字
     */
    String CONTAININTEGER    = "^.*[0-9]+.*$";
    /**
     * 全为半角字符
     */
    String FULLDBC           = "^[\\x00-\\xff]*$";
    /**
     * 含有半角字符
     */
    String CONTAINDBC        = "[\\x00-\\xff]{1}";
    /**
     * 含有特殊字符
     */
    String CONTAINSPECIAL    = "^.*[\\[|\\]|<|>|=|!|+|\\-|*|/|\\\\|%|_|(|)|'|\"|:|.|^|||,|;|$|&|~|#|{|}|?]+.*$";
    /**
     * 标准字符串
     */
    String STANDARSTRING     = "^(?:[\\u4e00-\\u9fa5]*\\w*\\s*)+$";
    /**
     * 严格合法的网址
     */
    String URLSTRICT         = "^(http://|https://)[^.].*\\.(\\w{3}|\\w{2}|\\w{4}|\\w{1})$";
    /**
     * 一般合法网址
     */
    String URL               = "^(www.)[^.]\\w+\\.(\\w{3}|\\w{2}|\\w{4}|\\w{1})$";
    /**
     * 合法ip
     */
    String IP                = "^((25[0-5]|2[0-4]\\d|1?\\d?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1?\\d?\\d)(:\\d+)?$";
    /**
     * 整数验证
     */
    String INTEGER           = "(^[-+]?[1-9]\\d*$)|(^0$)";
    /**
     * 负整数
     */
    String NEGATIVEINTEGER   = "^-[1-9]\\d*$";
    /**
     * 非正整数
     */
    String UNPOSITIVEINTEGER = "(^-[1-9]\\d*$)|(^0$)";
    /**
     * 正整数
     */
    String POSITIVEINTEGER   = "^[0-9]*[1-9][0-9]*$";
    /**
     * 非负整数
     */
    String UNNEGATIVEINTEGER = "\\d+";
    /**
     * 浮点数
     */
    String FLOAT             = "^[-+]?(0|([1-9]\\d*))\\.\\d+$";
    /**
     * 正浮点数
     */
    String POSITIVEFLOAT     = "^\\+?(0|([1-9]\\d*))\\.\\d+$";
    /**
     * 负浮点数
     */
    String NEGATIVEFLOAT     = "^-(0|([1-9]\\d*))\\.\\d+$";
    /**
     * 非负浮点数
     */
    String UNNEGATIVEFLOAT   = "^\\+?(0|([1-9]\\d*))\\.\\d+$";
    /**
     * 非正浮点数
     */
    String UNPOSITIVEFLOAT   = "^-(0|([1-9]\\d*))\\.\\d+$";
    /**
     * email规则
     */
    String EMAIL             = "^[a-zA-Z0-9]+([_.]?[a-zA-Z0-9])*@([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]{2,3}$";
    /**
     * 属性名称规则
     */
    String ATTRIBUTENAME     = "^([a-zA-Z]+\\.?[a-zA-Z]+)+$";
    /**
     * 英文姓名
     */
    String ENGLISHNAME       = "^[a-zA-Z\\s]+\\.?\\s?[a-zA-Z\\s]+$";
    /**
     * 身份证简单验证
     */
    String PID               = "^([1-9]\\d{17,17}|[1-9]\\d{14,14}|[1-9]\\d{16,16}X)$";
    /**
     * 身份证严格验证
     */
    String STANDARDPID       = "^([1-9][0-9]{5})(18|19|20)?(\\d{2})([01]\\d)([0123]\\d)(\\d{3})(\\d|X)?$";
    /**
     * 电话号码规则
     */
    String TEL               = "^\\d(-?\\d)*$";
    /**
     * 验证登录名
     */
    String LOGINID           = "^[a-zA-Z][A-Za-z0-9_]+$";
    /**
     * 密码格式
     */
    String PWFORMAT          = "^[\\x20-\\x7e]{6,18}$";
    /**
     * 全英文验证
     */
    String ALLENGLISH        = "^[A-Za-z]+$";
    /**
     * 全中文验证
     */
    String ALLCHINESE        = "^[\u4E00-\u9FA0]+$";
    /**
     * 全数字验证
     */
    String ALLNUMBER         = "\\d+";
    /**
     * 邮编规则
     */
    String POSTCODE          = "\\d{6}";
    /**
     * 区号验证
     */
    String CITYCODE          = "\\d{3}|\\d{4}";
    /**
     * 验证qq
     */
    String QQ                = "[1-9][0-9]{4,}";
    /**
     * 时间格式
     */
    String TIME              = "([01][0-9]|[2][0-3])\\:([0-5][0-9])(\\:([0-5][0-9]))?";
    /**
     * 所有字母大写
     */
    String ALLUPPERCASE      = "\\s*[A-Z][A-Z\\s]*";
    /**
     * 所有字母小写
     */
    String ALLLOWERCASE      = "\\s*[a-z][a-z\\s]*";
    /**
     * 日期格式
     */
    String DATE              = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";

    /**
     * 居民身份证号码15位或18位，最后一位可能是数字或字母
     */
    String CHINESE_PID = "[1-9]\\d{13,16}[a-zA-Z0-9]";

    /**
     *
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     */
    String MOBILE = "(\\+\\d+)?1[3458]\\d{9}$";

    /**
     * 固定电话号码
     */
    String PHONE_REGEX = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
}
