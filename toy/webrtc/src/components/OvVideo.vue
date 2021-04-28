<template>
  <video autoplay />
</template>

<script>
export default {
  name: 'OvVideo',
  props: {
    streamManager: Object,
  },
  mounted() {
    if (this.streamManager) this.streamManager.addVideoElement(this.$el);
  },
};
</script>
