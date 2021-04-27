<template lang="">
   <div class="NumPerson">
      <i class="fas fa-users"></i><span>{{ current }} / {{ limit }}</span>
   </div>
</template>
<script>
export default {
   props: {
      current: Number,
      limit: Number,
   },
};
</script>
<style scoped lang="scss">
.NumPerson {
   width: 55px;
   height: 20px;
   background-color: red;
}
</style>
