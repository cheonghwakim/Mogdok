import { createLoginInstance } from './index.js';

const instance = createLoginInstance();

// 예시
function example(data, success, fail) {
  instance
    .get(`example`, { params: data })
    .then(success)
    .catch(fail);
}

export { example };
