<template lang="">
   <div class="header">
      <h1>HEADER</h1>
   </div>
</template>
<script>
export default {
   name: 'Header',
   components: {},
   props: {},
   data() {
      return {};
   },
   computed: {},
   watch: {},
   //lifecycle area
   methods: {},
};
</script>
<style scoped lang="scss">
.header {
   background-color: rgb(255, 200, 200);
   width: 100%;
   height: 80px;

   flex-shrink: 0; // flex에 영향 없는 고정 요소 셋팅
}
</style>
