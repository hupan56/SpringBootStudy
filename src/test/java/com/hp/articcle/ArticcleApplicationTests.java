package com.hp.articcle;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hp.article.ArticcleApplication;
import com.hp.article.mapper.OperateArticleMapper;
import com.hp.article.pojo.Article;
import com.hp.article.pojo.Utils;
import com.hp.article.service.impl.OperateArticleServiceImpl;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@SpringBootTest(classes = ArticcleApplication.class)
class ArticcleApplicationTests {
    @Autowired
    OperateArticleMapper opam;

    @Autowired
    OperateArticleServiceImpl opai;

    @Autowired
    private Utils pageUtils;



    @Test
    void contextLoads() {
    }

    @Test
    void getArticleByKeyWord1() throws ParseException {

        Page<Article> page=new Page<>(1,5);
//
//        Page<Article> x = opam.getArticlesByKey(page,"习");

//        Page<Article> page = new Page<>(1, 5);
//        opam.getAll(page);
//        System.out.println(page.getRecords());
//        测试文章分类
//        System.out.println(opai.getAllArticleType());
//        System.out.println(opai.getAllArticleTime());
//        测试文章时间


        ArrayList<String> type = new ArrayList<>();
//        type.add("讲话");
        ArrayList<Integer> time = new ArrayList<>();
//        time.add(2020);
        ArrayList<String> field = new ArrayList<>();
//        field.add("生活");
        ArrayList<String> source= new ArrayList<>();

        source.add("《求是》");
//        System.out.println(opam.getArticlesByTags(page, "习", "不限",time,type,field,source));
//        System.out.println(opai.getArticlesByTags("习", "不限", time, type, field, source,1));


//        测试插入:成功;
//        Article article = new Article();
//        article.setArticleAuthor("习近平");
//        article.setArticleTitle("在庆祝香港回归祖国二十五周年大会暨香港特别行政区第六届政府就职典礼上的讲话");
//        article.setArticleEditor("任一林");
//        article.setArticleType("会议");
//        article.setArticleField("政治");
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String day="2022-07-05";
//        Date parse = simpleDateFormat.parse(day);
//        long time1 = parse.getTime();
//        java.sql.Date date = new java.sql.Date(time1);
//        article.setArticleTime(date);
//        article.setArticleContent("新华社北京7月4日电  中共中央总书记、国家主席、中央军委主席习近平《在庆祝香港回归祖国二十五周年大会暨香港特别行政区第六届政府就职典礼上的讲话》单行本，已由人民出版社出版，即日起在全国新华书店发行。\n" +
//                "\n" +
//                "《 人民日报 》（ 2022年07月05日 01 版）");
//        article.setArticleSource("人民网－人民日报");
//
//        System.out.println(opai.insert(article));
        System.out.println(opai.getAllByArticleTitleAndArticleSource("在庆祝香港回归祖国二十五周年大会暨香港特别行政区第六届政府就职典礼上的讲话",
                "人民网－人民日报"));

    }

}
