import { createDeskInstance } from './index.js';

const instance = createDeskInstance();

function getDeskInfo(params, success, fail) {
  instance
    .get(`desk/all`, { params })
    .then(success)
    .catch(fail);
}

function saveMemoList(params, success, fail) {
  instance
    .post(`desk/memo/update`, params)
    .then(success)
    .catch(fail);
}

function deleteMemoList(params, success, fail) {
  instance
    .post(`desk/memo/delete`, params)
    .then(success)
    .catch(fail);
}

export { getDeskInfo, saveMemoList, deleteMemoList };
