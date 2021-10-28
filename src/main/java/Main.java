import controller.DividendController;
import dto.DividendCalculated;
import dto.DividendRaw;
import org.jsoup.nodes.Document;
import output.xlsx.XlsWriter;
import parser.DividendParser;
import util.DataProvider;

import java.util.ArrayList;


public class Main {

    public static void main(String[] args) {

        //get document
        Document doc = DataProvider.getDocument();

        //get list of dividends
        ArrayList<DividendRaw> dividendList = DividendParser.parseDividends(doc);

        //calculate dividends
        ArrayList<DividendCalculated> list = DividendController.calculateDivs(dividendList);

        //write results in file
        XlsWriter.writeXlsFile(list);
    }
}
