<template>
    <div id="notFound">

        <v-container>
            <v-row>

                <v-col cols="12" class="d-flex justify-center mt-10">
                    <div>
                        <h2>존재하지 않는 페이지 입니다.</h2>
                    </div>



                </v-col>


            </v-row>

            <v-row>
                <v-col cols="12" class="d-flex justify-center mt-10">

                    <div>
                        <v-btn  x-large color="primary" dark :to="{ name: 'Home' }">
                            메인으로 돌아가기
                        </v-btn>
                    </div>
                </v-col>

            </v-row>


        </v-container>





    </div>

</template>

<script>
    export default {

    }
</script>

<style>

</style>