package com.example.baseapplication.mvp.model.event;

import android.net.Uri;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-25 13:57
 */
public class MatisseDataEvent {
    private List<Uri> uris;
    private List<String> strings;

    public MatisseDataEvent() {
    }

    public MatisseDataEvent(List<Uri> uris, List<String> strings) {
        this.uris = uris;
        this.strings = strings;
    }

    public List<Uri> getUris() {
        return uris;
    }

    public void setUris(List<Uri> uris) {
        this.uris = uris;
    }

    public List<String> getStrings() {
        return strings;
    }

    public void setStrings(List<String> strings) {
        this.strings = strings;
    }
}
