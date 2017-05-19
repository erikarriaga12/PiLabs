import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.TextFragment;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.queryparser.classic.QueryParser;

import java.io.IOException;
import java.nio.file.Paths;


/**
 * Created by plependu on 5/11/17.
 */
public class Searcher {
    public static final String INDEX_DIRECTORY = ;//<-- add path

	//example: "C:\\Users\\maro8\\Desktop\\tmp";

    public static void main(String[] args) throws IOException, ParseException, InvalidTokenOffsetsException {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(INDEX_DIRECTORY)));
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new StandardAnalyzer();

        String userQuery = args.length > 0 ? String.join(" ", args) : "congestive heart failure";
        QueryParser parser = new QueryParser("contents", analyzer);
        Query query = parser.parse(userQuery);
        TopDocs results = searcher.search(query, 100);
        ScoreDoc[] hits = results.scoreDocs;
        System.out.println(results.totalHits + " total matching documents");
        for (ScoreDoc hit : hits) {
            Document doc = searcher.doc(hit.doc);
            System.out.println(doc.get("path"));

            /* TODO: complete highlighter
            Highlighter highlighter = new Highlighter(new QueryScorer(query));
            TokenStream tokenStream = TokenSources.getTokenStream(doc, "content", analyzer);
            String text = doc.get("content");
            TextFragment[] frag = highlighter.getBestTextFragments(tokenStream, text, false, 4);
            System.out.println(frag);
            */
        }


    }
}
