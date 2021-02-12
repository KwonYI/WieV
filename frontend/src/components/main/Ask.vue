<template>
	<div id="ask">
			<div class="container" id="contact">
				<h2 class="mt-5">문의하기</h2>
			</div>
			<div class="container">
				<div class="row">
					<div class="col-md-6 col-sm-12">
						<h3 class="h5">플랫폼 도입 문의</h3>
						<form action="" method="">
							<div class="row no-gutters">
								<div class="col-lg-6 col-md-12 col-sm-12 p-1">
									<input class="bg-light form-control" type="text" name="name" placeholder="*회사 이름" id="ask1"
										required="required" />
								</div>
								<div class="col-lg-6 col-md-12 col-sm-12 p-1">
									<input class="bg-light form-control" type="email" name="_replyto" placeholder="*이메일" id="ask2"
										required="required" />
								</div>
								<div class="col-lg-6 col-md-12 col-sm-12 p-1">
									<input class="bg-light form-control" type="text" name="name" placeholder="*전화번호" id="ask3"
										required="required" />
								</div>
								<div class="col-lg-6 col-md-12 col-sm-12 p-1">
									<input class="bg-light form-control" type="text" name="name" placeholder="*사용기간" id="ask4"
										required="required" />
								</div>
							</div>
							<div class="row pb-2 no-gutters">
								<div class="col-md-12 col-sm-12">
									<textarea class="bg-light mb-2 form-control p-3" name="message" placeholder="*비지니스 요구에 대한 추가 정보" rows="5" id="ask5"
										required="required"></textarea>
								</div>
							</div>
							<button class="btn btn-primary" type="submit" @click="askForm('문의가 접수되었습니다. \n최대한 빠른 시일 내로 이메일 답변 드리겠습니다.\n감사합니다.',$event)">도입 문의</button>
						</form>
					</div>
					<div class="col-md-6 col-sm-12 float-right text-right">
						<h3 class="h5">Address</h3><span>서울특별시 강남구 테헤란로 212<br />멀티캠퍼스 역삼<br />South Korea</span>
						<p></p>
						<h3 class="h5">Phone</h3>
						<p>(+82)02-1212-1212</p>
						<h3 class="h5">Email</h3>
						<p>ssafytesta405@gmail.com</p>
					</div>
				</div>
			</div>



	</div>

</template>

<script>
	export default {
		name: "Ask",
		data: function () {
			return {

			}
		},

		methods: {
      askForm: function (message, event) {
        if (event) event.preventDefault();
        alert(message)
        document.getElementById("ask1").value = ''
        document.getElementById("ask2").value = ''
        document.getElementById("ask3").value = ''
        document.getElementById("ask4").value = ''
        document.getElementById("ask5").value = ''
      }


		},



		created: function () {

		},
		computed: {

		},
	}
</script>

<style scoped>

</style>