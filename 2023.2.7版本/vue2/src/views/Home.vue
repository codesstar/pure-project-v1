<template>
    <div>
        <el-row>
            <el-col :span="12">
                <div id="main" style="width:500px;height:400px"></div>
            </el-col>
            <el-col :span="12">
                <div id="pie" style="width:500px;height:400px"></div>
            </el-col>
        </el-row>
    </div>
</template>

<script>
   import * as echarts from 'echarts';
    export default {
        data() {
            return {
                key: value
            }
        },
        mounted(){  //页面渲染后才调用
            var chartDom = document.getElementById('main');
            var myChart = echarts.init(chartDom);
            var option;

            option = {
            xAxis: {
                type: 'category',
                data: ['第一季度', 'T第二季度', '第三季度', '第四季度']
            },
            yAxis: {
                type: 'value'
            },
            series: [
                {
                data: [],
                type: 'line'
                },
                {
                data: [],
                type: 'bar'
                }
            ]
              };

            this.request.get("/echarts/members").then(res =>{
                option.xAxis.data=res.data.x
                option.series[0].data=res.data
                option.series[1].data=res.data
            })
              myChart.setOption(option);

            //pie option
            var chartDom = document.getElementById('pie');
            var myChart = echarts.init(chartDom);
            var option;

            option = {
            title: {
                text: 'Referer of a Website',
                subtext: 'Fake Data',
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
                name: 'Access From',
                type: 'pie',
                radius: '50%',
                data: [
                    { value: 1048, name: 'Search Engine' },
                    { value: 735, name: 'Direct' },
                    { value: 580, name: 'Email' },
                    { value: 484, name: 'Union Ads' },
                    { value: 300, name: 'Video Ads' }
                ],
                emphasis: {
                    itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
                }
            ]
            };
            myChart.setOption(option);

        }
    }
</script>

<style lang="scss" scoped>

</style>