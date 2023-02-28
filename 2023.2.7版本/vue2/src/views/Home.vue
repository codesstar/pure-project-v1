<template>
    <div>
        <el-row :gutter="10" style="margin-bottom:20px">
            <el-col :span="6">
                <el-card>
                    <div style="color:#409EFF"><i class="el-icon-user"></i>      用户总数 </div>
                    <div style="padding:10px 0 ;text-align:center; font-weight:bold">100000</div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card>
                    <div style="color:#E6A23C"><i class="el-icon-goods"></i>  销售总量</div>
                    <div style="padding:10px 0 ;text-align:center; font-weight:bold">200000</div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card>
                    <div style="color:#67C23A"><i class="el-icon-money"></i>   收益总额</div>
                    <div style="padding:10px 0 ;text-align:center; font-weight:bold">300000</div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card>
                    <div style="color:#F56C6C"><i class="el-icon-s-claim"></i>   门店数目</div>
                    <div style="padding:10px 0 ;text-align:center; font-weight:bold">15</div>
                </el-card>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="12">
                <div id="main" style="width:500px;height:450px"></div>
            </el-col>
            <el-col :span="12">
                <div id="pie" style="width:500px;height:450px"></div>
            </el-col>
        </el-row>
    </div>
</template>

<script>
   import * as echarts from 'echarts';
    export default {
        // data() {
        //     return {
        //         key: value
        //     }
        // },
        mounted(){  //页面渲染后才调用
            
            
        var option = {
            xAxis: {
                type: 'category',
                data: ['第一季度', '第二季度', '第三季度', '第四季度']
            },
            yAxis: {
                type: 'value'
            },
            series: [
                {
                name:"星巴克",
                data: [],
                type: 'line'
                },
                {
                name:"星巴克",
                data: [],
                type: 'bar'
                },
                {
                name:"瑞幸",
                data: [1,2,3,4],
                type: 'line'
                },
                {
                name:"瑞幸",
                data: [1,2,3,4],
                type: 'bar'
                }
            ]
              };

            var chartDom = document.getElementById('main');
            var myChart = echarts.init(chartDom);

            this.request.get("/echarts/members").then(res =>{
                
                option.series[0].data=res.data
                 option.series[1].data=res.data
                 myChart.setOption(option);
                //这一步一定要放在request请求里面，因为request是异步请求，所以是比较慢执行的，如果挂载放在外面
                //那么就可能外面先挂载了，并不是实时渲染的话，里面再怎么修改已经没用了。所以要先修改后再挂载
            })

            
            var pieOption = {
            title: {
                text: '各季度会员数量统计',
                subtext: '比例图',
                left: 'center'
            },
            tooltip: {
                trigger: 'item'
            },
            legend: {
                orient: 'vertical',
                left: 'left'
            },
            series: [
                {
                type: 'pie',
                radius: '60%',
                label:{            //饼图图形上的文本标签
                    normal:{
                    show:true,
                    position:'inner', //标签的位置
                    textStyle : {
                        fontWeight : 300 ,
                        fontSize : 14,    //文字的字体大小
                        color: "#fff"
                    },
                    formatter:'{d}%'
                    }
                },
                data: [],  // 填空
                emphasis: {
                    itemStyle: {
                    //与阴影有关
                    shadowBlur: 10,
                    shadowOffsetX: 2,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
                }
            ]
            };
            var pieDom = document.getElementById('pie');
var pieChart = echarts.init(pieDom);

this.request.get("/echarts/members").then(res => {

    pieOption.series[0].data = [
        {name: "第一季度", value: res.data[0]},
        {name: "第二季度", value: res.data[1]},
        {name: "第三季度", value: res.data[2]},
        {name: "第四季度", value: res.data[3]},
    ]
    pieChart.setOption(pieOption)
})

     }
    }
</script>

<style lang="scss" scoped>

</style>