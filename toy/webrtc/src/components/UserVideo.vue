<template>
  <div class="seat pointer">
    <ov-video :stream-manager="streamManager" />
    <div>
      <p>{{ clientData }}</p>
    </div>
  </div>
</template>

<script>
import OvVideo from './OvVideo';
export default {
  name: 'UserVideo',
  components: {
    OvVideo,
  },
  props: {
    streamManager: Object,
  },
  computed: {
    clientData() {
      const clientData = this.getConnectionData();
      return clientData;
    },
  },
  methods: {
    getConnectionData() {
      if (!this.streamManager) return '빈자리';
      const { connection } = this.streamManager.stream;
      return connection.data;
    },
  },
};
</script>

<style scoped>
.seat {
  min-width: 320px;
  max-width: 320px;
  height: 240px;
  background-color: lightgray;
  border-radius: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  color: gray;
  margin: 10px;
}
.seat:hover {
  background-color: gray;
  color: white;
}
</style>
