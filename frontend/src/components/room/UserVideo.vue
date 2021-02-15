<template>
  <span v-if="streamManager" align="center">
    <ov-video :stream-manager="streamManager" />
    <p>{{ JSON.parse(streamManager.stream.connection.data.split("%/%")[0])['name'] }}</p>
  </span>
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
      clientData : undefined
    }
  },

  created() {
    if(this.streamManager){
      const { connection } = this.streamManager.stream;
      this.clientData = JSON.parse(connection.data.split("%/%")[0]);
    }
  },
};
</script>
<style>
.speaking{
  border-color: skyblue;
  border-style: solid;
}  
</style>
