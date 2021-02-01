<template>
  <div id="schedule">
    <!--스케줄 (공고 리스트) 컴포넌트 // 나중에 라우터로 변경 될 수 있다. -->
    <h3>반갑습니다. {{com_name}}님</h3>

    <div>
      <!--데이터 테이블 <v-data-table :headers="headers" :items="recruits" class="elevation-1">
        <template v-slot:[`item.re_flag`]="{ item }">
          <v-chip :color="getColor(item.re_flag)" dark>
            {{ item.calories }}
          </v-chip>
        </template>
      </v-data-table> -->

      <v-simple-table fixed-header class="mt-5">
      <thead>
        <tr>
          <th class="text-center">시즌</th>
          <th class="text-center">분류</th>
          <th class="text-center">기간</th>
          <th class="text-center">관리</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="recruit in recruitList"
          :key="recruit.reSeq"
          class="text-center"
        >
          <td>{{ recruit.reYear }} {{ recruit.reFlag }}</td>
          <td>{{ recruit.reStatus }}</td>
          <td>{{ recruit.reStartDate }} ~ {{recruit.reEndDate }}</td>
          <td><v-btn>관리하기</v-btn></td> <!--여기에 recruitMenu라우터 연결해야 합니다.-->
        </tr>
      </tbody>
    </v-simple-table>

    </div>

  </div>

</template>

<script>
  import { mapState } from "vuex";

  export default {
    name: "Schedule", 
    data: function () {
      return {
        com_name: "버즈글로벌",
      }
    },

    methods: {
      
    },
    created: function () {

    },
    computed: {
      ...mapState(["recruitList"]),
    },
  }
</script>

<style>
</style>