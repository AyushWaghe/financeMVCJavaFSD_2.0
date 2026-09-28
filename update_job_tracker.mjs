import fs from "node:fs/promises";
import { FileBlob, SpreadsheetFile } from "file:///C:/Users/AyushWaghe/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/@oai/artifact-tool/dist/artifact_tool.mjs";

const workbookPath = "C:/Users/AyushWaghe/Desktop/Job Tracker.xlsx";

const rowsToAdd = [
  [
    new Date(2026, 8, 22),
    "Infosys",
    "Java Springboot Developer",
    "Bengaluru East, Karnataka",
    "https://in.linkedin.com/jobs/view/java-springboot-developer-at-infosys-4459413520",
    "Applied",
  ],
  [
    new Date(2026, 8, 22),
    "SkilloVilla",
    "Java Backend Developer",
    "Greater Bengaluru Area",
    "https://in.linkedin.com/jobs/view/java-backend-developer-at-skillovilla-4462353992",
    "Applied",
  ],
];

const input = await FileBlob.load(workbookPath);
const workbook = await SpreadsheetFile.importXlsx(input);
const sheet = workbook.worksheets.getItem("Sheet1");

const used = sheet.getUsedRange();
const values = used.values;
const existingUrls = new Set(
  values
    .slice(1)
    .map((row) => String(row[4] ?? "").trim())
    .filter(Boolean),
);

const newRows = rowsToAdd.filter((row) => !existingUrls.has(row[4]));

if (newRows.length > 0) {
  const startRow = values.length + 1;
  const endRow = startRow + newRows.length - 1;
  const templateRange = sheet.getRange(`A${values.length}:F${values.length}`);
  const targetRange = sheet.getRange(`A${startRow}:F${endRow}`);
  targetRange.copyFrom(templateRange, "formats");
  targetRange.values = newRows;
  sheet.getRange(`A${startRow}:A${endRow}`).format.numberFormat = "dd-mm-yyyy";
}

workbook.recalculate();

const check = await workbook.inspect({
  kind: "table",
  range: `Sheet1!A1:F${values.length + newRows.length}`,
  include: "values,formulas",
  tableMaxRows: 30,
  tableMaxCols: 6,
});
console.log(check.ndjson);

const errors = await workbook.inspect({
  kind: "match",
  searchTerm: "#REF!|#DIV/0!|#VALUE!|#NAME\\?|#N/A|#NUM!|#NULL!|#SPILL!|#CALC!",
  options: { useRegex: true, maxResults: 300 },
  summary: "final formula error scan",
});
console.log(errors.ndjson);

const preview = await workbook.render({
  sheetName: "Sheet1",
  range: `A1:F${values.length + newRows.length}`,
  scale: 1,
  format: "png",
});
await fs.writeFile("job_tracker_preview.png", new Uint8Array(await preview.arrayBuffer()));

const output = await SpreadsheetFile.exportXlsx(workbook);
await output.save(workbookPath);

console.log(JSON.stringify({ appended: newRows.length, workbookPath }));
