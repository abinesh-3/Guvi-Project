package ExpenseTracker.ProjectExpenseTracker.service;


import ExpenseTracker.ProjectExpenseTracker.model.*;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.Phrase;


import java.io.ByteArrayOutputStream;
import java.util.List;


@Service
public class ReportService {
@SneakyThrows
public byte[] toPdf(List<Expense> items) {
Document doc = new Document(PageSize.A4);
ByteArrayOutputStream out = new ByteArrayOutputStream();
PdfWriter.getInstance(doc, out);
doc.open();
doc.add(new Paragraph("SmartSpend Report"));
PdfPTable t = new PdfPTable(5);
for (String h : new String[]{"Date","Title","Category","Type","Amount"}) t.addCell(new PdfPCell(new Phrase(h)));
for (Expense e: items) {
t.addCell(e.getDate().toString());
t.addCell(e.getTitle());
t.addCell(e.getCategory());
t.addCell(e.getType().name());
t.addCell(e.getAmount().toPlainString());
}
doc.add(t); doc.close();
return out.toByteArray();
}


@SneakyThrows
public byte[] toExcel(List<Expense> items) {
try (Workbook wb = new XSSFWorkbook()) {
Sheet s = wb.createSheet("SmartSpend");
int r=0; Row header = s.createRow(r++);
String[] cols = {"Date","Title","Category","Type","Amount"};
for (int i=0;i<cols.length;i++) header.createCell(i).setCellValue(cols[i]);
for (Expense e: items) {
Row row = s.createRow(r++);
row.createCell(0).setCellValue(e.getDate().toString());
row.createCell(1).setCellValue(e.getTitle());
row.createCell(2).setCellValue(e.getCategory());
row.createCell(3).setCellValue(e.getType().name());
row.createCell(4).setCellValue(e.getAmount().doubleValue());
}
for (int i=0;i<cols.length;i++) s.autoSizeColumn(i);
ByteArrayOutputStream out = new ByteArrayOutputStream();
wb.write(out); return out.toByteArray();
}
}
}