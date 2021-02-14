<template>
  <div v-if="streamManager">
    <ov-video :stream-manager="streamManager" />
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
      // length : 0
    }
  },

  created() {
      const { connection } = this.streamManager.stream;
      this.clientData = JSON.parse(connection.data.split("%/%")[0]);

      // if(this.clientData.type === 'viewee'){
      //   this.length = 6;
      // }else{
      //   this.length = 3;
      // }
  },
};
</script>
<style>
.speaking{
  border-color: red;
  border-style: solid;
}  
</style>
