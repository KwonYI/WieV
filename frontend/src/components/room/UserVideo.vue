<template>
  <div v-if="streamManager" :video-type = "clientData.type">
    <ov-video :stream-manager="streamManager" />
    <div>
      <p>{{ clientData.name }}</p>
      <p>{{ clientData.type }}</p>
    </div>
  </div>
</template>

<script>
import OvVideo from "./OvVideo";

export default {
  name: "UserVideo",

  components: {
    OvVideo,
  },

  props: {
    streamManager: Object,
  },

  data() {
    return {
      clientData : undefined,
    }
  },

  created() {
      const { connection } = this.streamManager.stream;
      this.clientData = JSON.parse(connection.data.split("%/%")[0]);
  },
};
</script>
