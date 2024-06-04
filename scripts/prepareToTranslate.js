const referenceFileShow = document.getElementById('referenceFileShow');
const fileListShow = document.getElementById('fileListShow');
const referenceFile = document.getElementById('referenceFile');
const convertButton = document.getElementById('convertButton');
const fileList = document.getElementById('fileList');
const uploadButton = document.getElementById('uploadButton');
const message = document.getElementById('message');
const errorMessage = document.getElementById('errorMessage');

export function showReferenceFile (){
  fileListShow.innerHTML = '';
  referenceFileShow.innerHTML = '';

  document.getElementById('fileList').addEventListener('change', (event) => {
    const selectedFiles = event.target.files;

    fileListShow.innerHTML = ''
    
    for (let i = 0; i < selectedFiles.length; i++) {
      const file = selectedFiles[i];
      const listItem = document.createElement('li');

      listItem.addEventListener('click', (clickEvent) => {
        const selectedFileName = clickEvent.currentTarget.textContent;
      
        referenceFileShow.innerHTML = selectedFileName;
        referenceFile.value = selectedFileName;
      });

      listItem.textContent = file.name;
      fileListShow.appendChild(listItem);
      uploadButton.disabled = true;
    }
  });
}

  convertButton.addEventListener('click', async (event) => {
    
    if (!validateArbFiles()){
      return;
    }

    if (referenceFile.value === ''){
      errorMessage.textContent = '* Please select your reference file.'
      return;
    }

    const arbFormData = new FormData();

    for (let i = 0; i < fileList.files.length; i++) {
      arbFormData.append('fileList', fileList.files[i]);
    }

    arbFormData.append('referenceFile', referenceFile.value);
    try {
      message.textContent = 'Waiting ...'
      const response = await fetch('https://arb-excel-converter-web.onrender.com/translate/prepare-translate', {
        method: 'POST',
        body: arbFormData
      });
    if (response.ok) {
        const data = await response.blob();
        const tempURL = window.URL.createObjectURL(data);
        const downloadLink = document.createElement('a');
        downloadLink.href = tempURL;
        downloadLink.download = "output.xlsx";
        downloadLink.click();
        window.URL.revokeObjectURL(tempURL);
        errorMessage.textContent = '';
        message.textContent = "Excel File Generated & Downloaded Successfully";

      } else {
        console.error('Error in server response:', errorMessage);
      }
    } catch (error) {
      console.error('Error uploading files:', error);
    }
  });


export function arbFilesSelect(){
    fileList.click();
  }

function validateArbFiles(){

  if (!fileList .files.length) {
    errorMessage.textContent = '* Please select one or more files to upload.';
    return false;
  }

  for (let i = 0; i < fileList.files.length; i++) {
    const file = fileList.files[i];
    const extension = file.name.split('.').pop().toLowerCase();

    if (extension !== 'arb'){
      errorMessage.textContent = '* Invalid file type. Only ARB extensions are allowed.';
      return false;
    }
  }
  errorMessage.textContent = '';
  return true;
}