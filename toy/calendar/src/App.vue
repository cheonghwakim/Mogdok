<template>
   <div id="app">
      <h1>스터디 캘린더 토이 프로젝트</h1>
      <hr />
      <study-calendar></study-calendar>
      <study-calendar2></study-calendar2>
   </div>
</template>

<script>
import StudyCalendar from './components/StudyCalendar.vue';
import StudyCalendar2 from './components/StudyCalendar2.vue';
export default {
   name: 'App',
   components: { StudyCalendar, StudyCalendar2 },
};
</script>

<style></style>
