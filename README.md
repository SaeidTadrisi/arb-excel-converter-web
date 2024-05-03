
[**ARBAndExcelConvertorWeb**](https://github.com/SaeidTadrisi/arb-excel-converter-web)

**About:**\
Prepare translations: Take a single ARB file or a list of ARB files as input, convert them for translation, and export the result as an Excel file.\
Convert translations: Take an Excel file containing translated content and export a ZIP archive containing the corresponding ARB files.

**Features:**
- Converts ARB files to Excel format
- Converts Excel files to ARB format
- Supports various Excel formats, including XLSX, XLS
- Support to convert several ARB files to one Excel file

**Using API:**
1. To use the (https://arb-excel-converter-web.onrender.com/translate/prepare-translate) endpoint, you must provide two parameters:
- ARB Files (List MultipartFile) as "fileList" Key: This parameter expects a list of multiple files in the ARB format. These files likely contain translatable resources.
- Reference File (String) as "referenceFile" Key: This parameter expects a string value representing the path to a reference file or its content used in the translation process.

![prepare-translate](https://github.com/SaeidTadrisi/arb-excel-converter-web/assets/108466072/eaf6ccba-bf27-4802-9f66-4c466bda7d66)


2. To use the (https://arb-excel-converter-web.onrender.com/translate/convert-translation) endpoint, you must provide one parameter:
- Excel File (MultipartFile) as "file" Key: This parameter expects a single file in the Excel format (.xls, .xlsx). The file likely contains translated content that needs to be converted into ARB files.

![convert-translation](https://github.com/SaeidTadrisi/arb-excel-converter-web/assets/108466072/d98dd357-09d0-4690-9140-c07ae4d943a0)
