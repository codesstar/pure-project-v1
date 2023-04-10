<template>
  <el-card style="width:500px">
    <el-form label-width="80px" size="small">
      <el-upload class="avatar-uploader" :action="'http://' + serverIp + ':9090/file/upload'" :headers="headers"
        :show-file-list="false" :on-success="handleAvatarSuccess">
        <img v-if="form.avatarUrl" :src="form.avatarUrl" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>
      <el-form-item label="标号">
        <el-input v-model="form.fid" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="名称">
        <el-input v-model="form.fname" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="价格">
        <el-input v-model="form.price" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="数量">
        <el-input v-model="form.fcount" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="评价">
        <el-input v-model="form.remark" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="save">确定</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import { serverIp } from '../../public/config'
export default {
  name: "Person",
  data() {
    return {
      form: {},
      serverIp: serverIp,
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      headers: {}
    }
  },
  created() {

    let user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : null
    if (user) {
      this.headers['token'] = user.token;  // 设置请求头
      this.headers['nihao'] = "xxx";
    }

    this.request.get("/user/username/" + this.user.fname).then(res => {
      if (res.code === '200') {
        this.form = res.data
        // this.form.avatarUrl="http://localhost:9090/file/24daad88009e468d980b27f62468fb85.png"
        console.log('1111')
        console.log(this.form)
        console.log('2222')
      }
    })
    //         this.getUser().then(res => {
    //   console.log(res)
    //   this.form = res
    // })
  },
  methods: {
    //     async getUser() {
    //         return (await this.request.get("/user/username/" + this.user.username)).data
    // },
    save() {
      // 这里就是下面，要在这里校验，合不合法element-ui给你规定好了
      this.request.post("/user", this.form).then(res => {
        if (res.code === '200') {
          //这里的什么fname username一定一定一定要分得清楚！！！
          this.request.get("/user/username/" + this.user.fname).then(res => {
            res.data.token = JSON.parse(localStorage.getItem("user")).token
            localStorage.setItem("user", JSON.stringify(res.data))

            console.log(JSON.parse(localStorage.getItem("user")))
          })
          this.$router.push("/user")

          setTimeout(fun, 100);

          function fun() {
            javascript: location.reload();
          }

          //自动刷新页面


          this.$message.success("保存成功")
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    handleAvatarSuccess(res) {
      console.log("11111")
      console.log(res)
      this.form.avatarUrl = res
      console.log("xxxxxxxxxxxxx")
      console.log(this.form.avatarUrl)
      console.log(this.form)
    }
  }


}
</script>

<style>
.avatar-uploader {
  text-align: center;
  padding-bottom: 10px;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 138px;
  height: 138px;
  line-height: 138px;
  text-align: center;
}

.avatar {
  width: 138px;
  height: 138px;
  display: block;
}
</style>