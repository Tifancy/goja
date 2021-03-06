package goja.jxls.transformation;

import com.google.common.collect.Lists;
import goja.jxls.formula.CellRef;
import goja.jxls.model.Block;
import goja.jxls.model.Point;
import org.apache.poi.ss.util.CellReference;

import java.util.List;

/**
 * @author Leonid Vysochyn
 */
public class DuplicateTransformationByColumns extends BlockTransformation {

    int colNum;
    int duplicateNumber;
    List<String> cells = Lists.newArrayList();

    public DuplicateTransformationByColumns(Block block, int duplicateNumber) {
        super(block);
        this.duplicateNumber = duplicateNumber;
    }

    public Block getBlockAfterTransformation() {
        return null;
    }

    public List transformCell(Point p) {
        List<Point> resultCells;
        if (block.contains(p)) {
            resultCells = Lists.newArrayList();
            Point rp = p;
            resultCells.add(p);
            for (int i = 0; i < duplicateNumber; i++) {
                resultCells.add(rp = rp.shift(0, block.getNumberOfColumns()));
            }
        } else {
            resultCells = Lists.newArrayList();
            resultCells.add(p);
        }
        return resultCells;
    }

    public String getDuplicatedCellRef(String sheetName, String cell, int duplicateBlock) {
        CellReference cellRef = new CellReference(cell);
        int row = cellRef.getRow();
        short col = cellRef.getCol();
        String refSheetName = cellRef.getSheetName();
        String resultCellRef = cell;
        if (block.getSheet().getSheetName().equalsIgnoreCase(refSheetName) || (refSheetName == null && block.getSheet().getSheetName().equalsIgnoreCase(sheetName))) {
            // sheet check passed
            if (block.contains(row, col) && duplicateNumber >= 1 && duplicateNumber >= duplicateBlock) {
                col += block.getNumberOfColumns() * duplicateBlock;
                resultCellRef = cellToString(row, col, refSheetName);
            }
        }
        return resultCellRef;
    }

    public List transformCell(String sheetName, CellRef cellRef) {
        String refSheetName = cellRef.getSheetName();
        cells.clear();
        if (block.getSheet().getSheetName().equalsIgnoreCase(refSheetName) || (refSheetName == null && block.getSheet().getSheetName().equalsIgnoreCase(sheetName))) {
            // sheet check passed
            if (block.contains(cellRef.getRowNum(), cellRef.getColNum()) /*&& duplicateNumber >= 1*/) {
                colNum = cellRef.getColNum();
                cells.add(cellToString(cellRef.getRowNum(), colNum, refSheetName));
                for (int i = 0; i < duplicateNumber; i++) {
                    colNum += block.getNumberOfColumns();
                    cells.add(cellToString(cellRef.getRowNum(), colNum, refSheetName));
                }
            }
        }
        return cells;
    }

    public String cellToString(int row, int col, String sheetName) {
        String cellname;
        CellReference cellReference = new CellReference(row, col, false, false);
        if (sheetName != null) {
            cellname = sheetName + "!" + cellReference.formatAsString();
        } else {
            cellname = cellReference.formatAsString();
        }
        return cellname;
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof DuplicateTransformationByColumns) {
            DuplicateTransformationByColumns dt = (DuplicateTransformationByColumns) obj;
            return (super.equals(obj) && dt.duplicateNumber == duplicateNumber);
        }
        return false;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 29 * result + duplicateNumber;
        return result;
    }

    public String toString() {
        return "DuplicateTransformation: {" + super.toString() + ", duplicateNumber=" + duplicateNumber + "}";
    }
}
