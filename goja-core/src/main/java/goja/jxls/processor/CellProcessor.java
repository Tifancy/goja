package goja.jxls.processor;

import goja.jxls.parser.Cell;

import java.util.Map;


/**
 * Allows dynamic processing of cell in excel workbook
 * @author Leonid Vysochyn
 */
public interface CellProcessor {
    /**
     * This method is invoked when cell is processed
     *
     * @param cell   {@link Cell} object with information about cell
     * @param namedCells Map with information about all named cells processed before
     */
    void processCell(final Cell cell, final Map namedCells);
}
