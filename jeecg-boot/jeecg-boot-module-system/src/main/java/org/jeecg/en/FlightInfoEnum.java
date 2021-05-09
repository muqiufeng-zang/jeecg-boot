package org.jeecg.en;

/**
 * @Description:
 * @Author: qiufeng
 * @Date: Created in 2021/3/18
 */
public enum FlightInfoEnum {

    ETH("071", "埃塞俄比亚航空公司", "Ethiopian airlines", "ET", "ETH", "埃塞俄比亚"),
    GFA("072", "海湾航空公司", "Gulf Air", "GF", "GFA", "巴林");

    private String prefix;
    private String flightName;
    private String flightEnglishName;
    private String twoCode;
    private String threeCode;
    private String location;

    FlightInfoEnum(String prefix, String flightName, String flightEnglishName, String twoCode, String threeCode, String location) {
        this.prefix = prefix;
        this.flightName = flightName;
        this.flightEnglishName = flightEnglishName;
        this.twoCode = twoCode;
        this.threeCode = threeCode;
        this.location = location;
    }

    public static FlightInfoEnum toNum(String prefix) throws Exception {
        FlightInfoEnum[] var = values();
        for (int i = 0; i < var.length; i++) {
            FlightInfoEnum en = var[i];
            if (en.prefix.equals(prefix)) {
                return en;
            }
        }
        throw new Exception("运单航司不存在，请检查数据！");
    }

    public String getFlightName() {
        return flightName;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getFlightEnglishName() {
        return flightEnglishName;
    }

    public String getTwoCode() {
        return twoCode;
    }

    public String getThreeCode() {
        return threeCode;
    }

    public String getLocation() {
        return location;
    }
}
