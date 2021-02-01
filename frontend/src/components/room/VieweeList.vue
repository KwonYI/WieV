<template>
  <div id="VieweeList">


    <v-container class="grey lighten-5">
      <v-row no-gutters>
        <template v-for="n in 7">
          <v-col :key="n">
            <div class="mx-2 mt-2 vieweeFace" outlined tile>
              
              
            </div>
          </v-col>
          <v-responsive v-if="n%2 === 0" :key="`width-${n}`" width="100%"></v-responsive>
        </template>
      </v-row>
    </v-container>



  </div>

</template>

<script>
  export default {
    name: "VieweeList",

  }
</script>

<style scoped>
  .vieweeFace {
    border: 3px solid black;
    height: 180px;
  }
</style>