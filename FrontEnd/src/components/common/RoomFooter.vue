<template lang="">
   <div class="footer">
      <h1>FOOTER</h1>
   </div>
</template>
<script>
export default {
   name: 'Footer',
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
.footer {
   background-color: rgb(189, 210, 255);
   width: 100%;
   height: 190px;

   flex-shrink: 0; // flex에 영향 없는 고정 요소 셋팅
}
</style>
