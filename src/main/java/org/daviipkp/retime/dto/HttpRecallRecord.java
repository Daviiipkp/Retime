package org.daviipkp.retime.dto;

public record HttpRecallRecord(
    String url,
    String endpoint,
    String content,
    String type
) {

}
