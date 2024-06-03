const referenceFileShow = document.getElementById('referenceFileShow');
const fileListShow = document.getElementById('fileListShow');
const referenceFile = document.getElementById('referenceFile');
const convertButton = document.getElementById('convertButton');
const fileList = document.getElementById('fileList');
const uploadButton = document.getElementById('uploadButton');


export function showReferenceFile (){
  fileListShow.innerHTML = '';
  referenceFileShow.innerHTML = '';

  document.getElementById('fileList').addEventListener('change', (event) => {
    const selectedFiles = event.target.files;
    
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

export function sendARBRequest() {
  
  convertButton.addEventListener('click', async (event) => {
    
    if (!validateArbFiles()){
      return;
    }

    const arbFormData = new FormData();

    for (let i = 0; i < fileList.files.length; i++) {
      arbFormData.append('fileList', fileList.files[i]);
    }

    arbFormData.append('referenceFile', referenceFile.value);
    try {
        const response = await fetch('https://arb-excel-converter-web-cors.onrender.com/translate/prepare-translate', {
            method: 'POST',
            body: arbFormData
        });
        if (response.ok) {
          const data = await response.blob();
        } else {
          console.error('Error in server response:', errorMessage);
        }
      } catch (error) {
        console.error('Error uploading files:', error);
      }
    });
}

export function arbFilesSelect(){
  uploadButton.addEventListener('click', () => {
    fileList.click();
  })
}

function validateArbFiles(){
  const errorMessage = document.getElementById('errorMessage');

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