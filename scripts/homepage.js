import { showReferenceFile, sendARBRequest } from './prepareToTranslate.js';
import { showSelectedFile, sendExcelRequest } from './convertTranslation.js';

const hintElement = document.getElementById('hint');
const fileList = document.getElementById('fileList');
const selectElement = document.getElementById('selectOption');
const fileListShow = document.getElementById('fileListShow');
const referenceFileShow = document.getElementById('referenceFileShow');
const uploadButton = document.getElementById('uploadButton');
const excelFile = document.getElementById('excelFile');
const errorMessage = document.getElementById('errorMessage');
const message = document.getElementById('message');
const referenceFile = document.getElementById('referenceFile');
const convertButton = document.getElementById('convertButton');
const resetButton = document.getElementById('resetButton');

selectElement.addEventListener('change', handleOptionChange);
uploadButton.addEventListener('click', handleUploadButtonClick);
convertButton.addEventListener('click', handleConvertButtonClick);
resetButton.addEventListener('click', resetForm);

function handleOptionChange() {
  const selectedValue = selectElement.value;
  let hintText;

  if (selectedValue === 'prepare_to_translate') {
    hintText = 'Your files must be in standard (.ARB) format.';
  } else if (selectedValue === 'convert_translation') {
    hintText = 'Your file must be in standard (.Excel) format.';
  } else {
    hintText = 'You selected Wrong files!.';
  }

  hintElement.textContent = hintText;
}

function handleUploadButtonClick() {
  const selectedValue = selectElement.value;

  if (selectedValue === 'prepare_to_translate') {
    fileList.accept = '.arb';
    selectElement.disabled = true;
    fileList.click();
    showReferenceFile();
  } else if (selectedValue === 'convert_translation') {
    excelFile.accept = '.xlsx, .xls';
    selectElement.disabled = true;
    excelFile.click();
    showSelectedFile();
  } else {
    hintElement.textContent = 'You selected Wrong files!.';
  }
}

function handleConvertButtonClick() {
  const selectedValue = selectElement.value;

  if (selectedValue === 'prepare_to_translate') {
    sendARBRequest();
  } else if (selectedValue === 'convert_translation') {
    sendExcelRequest();
  }
}

function resetForm() {
  selectElement.selectedIndex = 0;
  selectElement.disabled = false;
  hintElement.textContent = '';
  fileListShow.textContent = '';
  fileList.value = '';
  excelFile.value = '';
  referenceFile.value = '';
  referenceFileShow.textContent = '';
  uploadButton.disabled = false;
  errorMessage.textContent = '';
  message.textContent = '';
}