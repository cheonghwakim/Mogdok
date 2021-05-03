import Vue from 'vue';

// truncate: text문자열의 길이가 length를 넘어가면 그 뒤 문자열을 suffix로 변경
// ex) '테스트문자열', 3, '..' => 테스트..
Vue.filter('truncate', (text, length, suffix) => {
  if (text.length > length) {
    return text.substring(0, length) + suffix;
  } else {
    return text;
  }
});
