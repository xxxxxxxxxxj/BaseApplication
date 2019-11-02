package com.example.baseapplication.mvp.model.entity;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/8/7 09:53
 */
public class EncyclopediasTitleBean {

    private List<EncyclopediasTitle> encyclopediaClassifications;

    public List<EncyclopediasTitle> getEncyclopediaClassifications() {
        return encyclopediaClassifications;
    }

    public void setEncyclopediaClassifications(List<EncyclopediasTitle> encyclopediaClassifications) {
        this.encyclopediaClassifications = encyclopediaClassifications;
    }

    public static class EncyclopediasTitle {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
