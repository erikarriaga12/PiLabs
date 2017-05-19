import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Created by plependu on 5/11/17.
 *
 * Lucene Indexer example: creates and indexes some files for illustration.
 */


public class Indexer {
    public static final Version LUCENE_CURRENT = Version.LUCENE_6_5_1;
    public static final String INDEX_DIRECTORY = ; //Add path here

//"C:\\Users\\maro8\\Desktop\\tmp";
//
    public static final String DATA_BUCKET = ;//Add path here
           // "C:\\Users\\maro8\\Desktop\\Work\\data\\bucket";

    private static Document getDocument(File file) throws IOException {
        Field filename = new StringField("path", file.getAbsolutePath(), Field.Store.YES);
        Field contents = new TextField("contents", Files.newBufferedReader(file.toPath()));
        Document doc = new Document();
        doc.add(filename);
        doc.add(contents);
        return doc;
    }

    public static void main(String[] args) throws IOException {
        long starttime = System.currentTimeMillis();
        System.out.println("start time: " + starttime);
        long time;

        StandardAnalyzer analyzer = new StandardAnalyzer();
        Directory index = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(index,config);

        int start = 0;
        int numDocs = 0;

        File bucket = new File(DATA_BUCKET);
        File[] datafiles = bucket.listFiles();
        for (File file : Arrays.asList(datafiles)) {
            String report = new String(Files.readAllBytes(Paths.get(file.getPath())));
            Document doc = getDocument(file);
            numDocs++;
            if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
                System.out.println("adding " + file);
                writer.addDocument(doc);
            } else {
                System.out.println("updating " + file);
                writer.updateDocument(new Term("path", file.getPath()), doc);
            }
        }
        writer.close();
    }
}


