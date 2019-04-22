package cn.buildworld.onenet.bean;

import java.util.List;

/**
 * @Author MiChong
 * @Email: 1564666023@qq.com
 * @Create 2018-04-09 16:35
 * @Version: V1.0
 */

public class DataPoints {


    /**
     * errno : 0
     * data : {"count":20,"datastreams":[{"datapoints":[{"at":"2018-04-01 16:27:33.799","value":"27.1"},{"at":"2018-04-01 16:26:26.993","value":"27.1"},{"at":"2018-04-01 16:25:22.181","value":"27.1"},{"at":"2018-04-01 16:24:15.362","value":"27.1"},{"at":"2018-04-01 16:23:08.559","value":"27.1"},{"at":"2018-04-01 16:22:01.759","value":"27.1"},{"at":"2018-04-01 16:20:56.949","value":"27.1"},{"at":"2018-04-01 16:19:52.123","value":"27.1"},{"at":"2018-04-01 16:18:45.306","value":"27.1"},{"at":"2018-04-01 16:17:38.497","value":"27.1"},{"at":"2018-04-01 16:16:31.697","value":"27.1"},{"at":"2018-04-01 16:15:26.877","value":"27.2"},{"at":"2018-04-01 16:14:20.074","value":"27.2"},{"at":"2018-04-01 16:13:13.270","value":"27.2"},{"at":"2018-04-01 16:12:08.455","value":"27.2"},{"at":"2018-04-01 16:11:01.649","value":"27.2"},{"at":"2018-04-01 16:09:54.845","value":"27.2"},{"at":"2018-04-01 16:08:48.043","value":"27.2"},{"at":"2018-04-01 16:07:41.235","value":"27.2"},{"at":"2018-04-01 16:06:34.429","value":"27.2"}],"id":"temperature"}]}
     * error : succ
     */

    private int errno;
    private DataBean data;
    private String error;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static class DataBean {
        /**
         * count : 20
         * datastreams : [{"datapoints":[{"at":"2018-04-01 16:27:33.799","value":"27.1"},{"at":"2018-04-01 16:26:26.993","value":"27.1"},{"at":"2018-04-01 16:25:22.181","value":"27.1"},{"at":"2018-04-01 16:24:15.362","value":"27.1"},{"at":"2018-04-01 16:23:08.559","value":"27.1"},{"at":"2018-04-01 16:22:01.759","value":"27.1"},{"at":"2018-04-01 16:20:56.949","value":"27.1"},{"at":"2018-04-01 16:19:52.123","value":"27.1"},{"at":"2018-04-01 16:18:45.306","value":"27.1"},{"at":"2018-04-01 16:17:38.497","value":"27.1"},{"at":"2018-04-01 16:16:31.697","value":"27.1"},{"at":"2018-04-01 16:15:26.877","value":"27.2"},{"at":"2018-04-01 16:14:20.074","value":"27.2"},{"at":"2018-04-01 16:13:13.270","value":"27.2"},{"at":"2018-04-01 16:12:08.455","value":"27.2"},{"at":"2018-04-01 16:11:01.649","value":"27.2"},{"at":"2018-04-01 16:09:54.845","value":"27.2"},{"at":"2018-04-01 16:08:48.043","value":"27.2"},{"at":"2018-04-01 16:07:41.235","value":"27.2"},{"at":"2018-04-01 16:06:34.429","value":"27.2"}],"id":"temperature"}]
         */

        private int count;
        private List<DatastreamsBean> datastreams;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<DatastreamsBean> getDatastreams() {
            return datastreams;
        }

        public void setDatastreams(List<DatastreamsBean> datastreams) {
            this.datastreams = datastreams;
        }

        public static class DatastreamsBean {
            /**
             * datapoints : [{"at":"2018-04-01 16:27:33.799","value":"27.1"},{"at":"2018-04-01 16:26:26.993","value":"27.1"},{"at":"2018-04-01 16:25:22.181","value":"27.1"},{"at":"2018-04-01 16:24:15.362","value":"27.1"},{"at":"2018-04-01 16:23:08.559","value":"27.1"},{"at":"2018-04-01 16:22:01.759","value":"27.1"},{"at":"2018-04-01 16:20:56.949","value":"27.1"},{"at":"2018-04-01 16:19:52.123","value":"27.1"},{"at":"2018-04-01 16:18:45.306","value":"27.1"},{"at":"2018-04-01 16:17:38.497","value":"27.1"},{"at":"2018-04-01 16:16:31.697","value":"27.1"},{"at":"2018-04-01 16:15:26.877","value":"27.2"},{"at":"2018-04-01 16:14:20.074","value":"27.2"},{"at":"2018-04-01 16:13:13.270","value":"27.2"},{"at":"2018-04-01 16:12:08.455","value":"27.2"},{"at":"2018-04-01 16:11:01.649","value":"27.2"},{"at":"2018-04-01 16:09:54.845","value":"27.2"},{"at":"2018-04-01 16:08:48.043","value":"27.2"},{"at":"2018-04-01 16:07:41.235","value":"27.2"},{"at":"2018-04-01 16:06:34.429","value":"27.2"}]
             * id : temperature
             */

            private String id;
            private List<DatapointsBean> datapoints;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public List<DatapointsBean> getDatapoints() {
                return datapoints;
            }

            public void setDatapoints(List<DatapointsBean> datapoints) {
                this.datapoints = datapoints;
            }

            public static class DatapointsBean {
                /**
                 * at : 2018-04-01 16:27:33.799
                 * value : 27.1
                 */

                private String at;
                private String value;

                public String getAt() {
                    return at;
                }

                public void setAt(String at) {
                    this.at = at;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }
            }

            @Override
            public String toString() {
                return "DatastreamsBean{" +
                        "id='" + id + '\'' +
                        ", datapoints=" + datapoints +
                        '}';
            }
        }
    }

}
