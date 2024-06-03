const referenceFileShow = document.getElementById('referenceFileShow');
const fileListShow = document.getElementById('fileListShow');
const excelFile = document.getElementById('excelFile');
const uploadButton = document.getElementById('uploadButton');



export function showSelectedFile() {
  fileListShow.innerHTML = '';
  referenceFileShow.innerHTML = '';

  excelFile.addEventListener('change', (event) => {
    const selectedFile = event.target.files;
    const file = selectedFile[0];
      fileListShow.innerHTML = file.name;
      uploadButton.disabled = true;
    });
}

export function sendExcelRequest() {
  convertButton.addEventListener('click', async (event) => {
    event.preventDefault();
    
    if (!validateExcelFiles()){
      return;
    }

    const excelFormData = new FormData();

    excelFormData.append('file', fileList.files[0]);

    try {
        const response = await fetch('https://arb-excel-converter-web-cors.onrender.com/translate/convert-translation', {
            method: 'POST',
            body: excelFormData
        });

        if (response.ok) {            
            const zipBlob = await response.blob();
        } else {
        }
    } catch (error) {
        console.error('Error uploading files:', error);
    }
});
}

export function excelFilesSelect(){
  uploadButton.addEventListener('click', () => {
    excelFile.click();
  })
}

function validateExcelFiles(){
  const errorMessage = document.getElementById('errorMessage');

  if (!excelFile.files.length) {
    errorMessage.textContent = '* Please select one file to upload.';
    return false;
  }

    const file = excelFile.files[0];
    const extension = file.name.split('.').pop().toLowerCase();

    if (extension !== 'xlsx'){
      errorMessage.textContent = '* Invalid file type. Only XLSX or XLS extensions are allowed.';
      return false;
    }
    errorMessage.textContent = '';
  return true;
}