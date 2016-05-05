package com.appealprocess.appeals.model;

import javax.xml.bind.annotation.XmlEnumValue;

public enum Section {
    @XmlEnumValue(value="online")
    ONLINE,
    @XmlEnumValue(value="inPerson")
    IN_PERSON
}
