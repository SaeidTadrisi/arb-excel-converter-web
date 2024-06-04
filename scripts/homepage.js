
import {showReferenceFile, sendARBRequest, arbFilesSelect} from './prepareToTranslate.js';
import {showSelectedFile, sendExcelRequest, excelFilesSelect} from './convertTranslation.js';

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



selectElement.addEventListener('change', executeBySelectedOption);

function executeBySelectedOption(){
  let hintText;
  const selectedValue = selectElement.value;
  console.log();
  if (selectedValue === 'prepare_to_translate') {
    hintText = 'Your files must be in standard (.ARB) format.';
  }
  else if (selectedValue === 'convert_translation'){
    hintText = 'Your file must be in standard (.Excel) format.';
  }
  else{
    hintText = 'You selected Wrong files!.';
  }
  hintElement.textContent = hintText;
};

uploadButton.addEventListener('click', uploadButtonAction)

function uploadButtonAction() {
  const selectedValue = selectElement.value;
  if (selectedValue === 'prepare_to_translate') {
    fileList.accept = ".arb"
    selectElement.disabled = true;
    arbFilesSelect();
    showReferenceFile();
    }
    else if (selectedValue === 'convert_translation'){
    excelFile.accept = ".xlsx , .xls"
    selectElement.disabled = true;
    excelFilesSelect();
    showSelectedFile();
  }
}

const convertButton = document.getElementById('convertButton');

convertButton.addEventListener('click', convertButtonAction)

function convertButtonAction (){
  const selectedValue = selectElement.value;
  if (selectedValue === 'prepare_to_translate') {
    sendARBRequest();
    }
    else if (selectedValue === 'convert_translation'){
    sendExcelRequest();
  }

}

const resetButton = document.getElementById('reset-button');
  resetButton.addEventListener('click', resetForm);

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