package com.appealprocess.appeals.model;

import javax.xml.bind.annotation.XmlEnumValue;


public enum AppealStatus {
    @XmlEnumValue(value="unprocessed")
    UNPROCESSED,
    @XmlEnumValue(value="processing")
    PROCESSING, 
    @XmlEnumValue(value="processed")
    PROCESSED,
    @XmlEnumValue(value="filed")
    FILED
}
