package com.aFeng;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BossJsonp {

    static final String Cookie = "__zp__pub__=; lastCity=101210100; _bl_uid=R9ka5d0LgdIp4wxO0r7Ob12dg4py; t=dPk2cIxdRh46WG1h; wt=dPk2cIxdRh46WG1h; __g=-; Hm_lvt_194df3105ad7148dcf2b98a91b5e727a=1597168486,1597296373,1597320901,1597333070; JSESSIONID=\"\"; __c=1597333070; __l=l=%2Fwww.zhipin.com%2Fhangzhou%2F%3Fka%3Dheader-home-logo&r=&g=; __a=19520399.1596592445.1597320901.1597333070.136.10.6.112; Hm_lpvt_194df3105ad7148dcf2b98a91b5e727a=1597334298; __zp_stoken__=6dd8aI2B9EDxgVwgcGW8Ych8WCltsDScKPEBIAG4RPkBIRAVFan8NeHY9GhNpDFhlfl0ZOyVEFG8GO0Q8AGo9DWhEd2QfDFEsfyBgTSNiXj5dOioNKQEhThBccjUQWHguPGRcRD9sNxg2fkU%3D";

    static final String Accept = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9";

    public static void main(String[] args) throws IOException {

        Document doc = Jsoup.connect("https://www.zhipin.com/job_detail/?ka=header-job")
                .timeout(6000)
                .header("accept",Accept)
                .header("cookie",Cookie)
                .get();
        Elements element = doc.getElementsByClass("job-list");
        Elements elementsByClass = element.get(0).getElementsByClass("primary-box");
        for(Element element1:elementsByClass){
            String url = "https://www.zhipin.com"+element1.attr("href");
            Document doc1 = Jsoup.connect(url)
                    .timeout(6000)
                    .header("accept",Accept)
                    .header("cookie",Cookie)
                    .get();
            String url1 = "";
            try {
                url1 = "https://www.zhipin.com" + doc1.getElementsByClass("btn btn-startchat").get(0).attr("data-url").trim().split("\\?")[0];
            }finally {

            }
            if(url.equals("")){
                continue;
            }
            Document doc2 = Jsoup.connect(url1)
                    .ignoreContentType(true)
                    .timeout(6000)
                    .header("accept",Accept)
                    .header("cookie",Cookie)
                    .data("jobId","?a4929a529ed641ac0XN52di8FFs~&lid=nlp-ayEHPMghiB7.search.1")
                    .get();
            System.out.println(doc2);
        }
    }

    static Map<String,String> s2m(String string){
        Map<String ,String > map = new HashMap<>();
        map.put("__l","l=%2Fwww.zhipin.com%2Fjob_detail%2F&r=&g=&friend_source=0");
        String[] strs = string.split(";");
        for(String ky:strs){
            String[] kys = ky.split("=");
            if(kys.length>1){
                map.put(ky.split("=")[0].trim(),ky.split("=")[1].trim());
            }else{
                map.put(ky.split("=")[0].trim(),"");
            }
        }
//        for(String key:map.keySet()){
//            System.out.println(key+"\t"+map.get(key));
//        }
        return map;
    }

}
