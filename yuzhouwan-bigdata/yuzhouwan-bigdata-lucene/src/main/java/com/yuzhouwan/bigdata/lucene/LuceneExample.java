package com.yuzhouwan.bigdata.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;

import java.nio.file.Paths;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：A simple example for lucene
 *
 * @author Benedict Jin
 * @since 2019/12/1
 */
public class LuceneExample {

    public static void main(String[] args) throws Exception {
        // index
        try (Directory index = new NIOFSDirectory(Paths.get("/tmp/index"))) {
            // add
            try (IndexWriter writer = new IndexWriter(index, new IndexWriterConfig(new StandardAnalyzer()))) {
                Document doc = new Document();
                doc.add(new TextField("blog", "yuzhouwan.com", Field.Store.YES));
                doc.add(new StringField("github", "asdf2014", Field.Store.YES));
                writer.addDocument(doc);
                writer.commit();
            }
            // search
            try (DirectoryReader reader = DirectoryReader.open(index)) {
                IndexSearcher searcher = new IndexSearcher(reader);
                QueryParser parser = new QueryParser("blog", new StandardAnalyzer());
                Query query = parser.parse("yuzhouwan.com");
                ScoreDoc[] hits = searcher.search(query, 1000).scoreDocs;
                for (ScoreDoc hit : hits) {
                    Document hitDoc = searcher.doc(hit.doc);
                    System.out.println(hitDoc.get("blog"));
                }
            }
        }
    }
}
