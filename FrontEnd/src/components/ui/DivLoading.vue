<template lang="">
   <div class="loading">
      로딩중
   </div>
</template>
<script>
export default {};
</script>
<style scoped lang="scss">
.loading {
   position: fixed;
   top: 50%;
   left: 50%;
   transform: translate(-50%, -50%);

   width: 500px;
   height: 500px;

   background-color: rgba(255, 0, 0, 0.201);
   z-index: 30;
}
</style>
