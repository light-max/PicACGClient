package com.lfq.picacg.data;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Data;

import java.util.List;

@Data
public class ContentRequest {

    /**
     * code : 0
     * time : 1565098485077
     * content : [{"images":["http://192.168.0.104:8080/image/source/8/24/0","http://192.168.0.104:8080/image/source/8/24/1","http://192.168.0.104:8080/image/source/8/24/2","http://192.168.0.104:8080/image/source/8/24/3","http://192.168.0.104:8080/image/source/8/24/4","http://192.168.0.104:8080/image/source/8/24/5","http://192.168.0.104:8080/image/source/8/24/6","http://192.168.0.104:8080/image/source/8/24/7","http://192.168.0.104:8080/image/source/8/24/8"],"star":0,"author":8,"show":["http://192.168.0.104:8080/image/show/8/24/0","http://192.168.0.104:8080/image/show/8/24/1","http://192.168.0.104:8080/image/show/8/24/2","http://192.168.0.104:8080/image/show/8/24/3","http://192.168.0.104:8080/image/show/8/24/4","http://192.168.0.104:8080/image/show/8/24/5","http://192.168.0.104:8080/image/show/8/24/6","http://192.168.0.104:8080/image/show/8/24/7","http://192.168.0.104:8080/image/show/8/24/8"],"title":"只狼忍杀截图","number":9,"releasetime":1565098310827,"watch":1,"authorname":"三号测试员","id":24,"keyword":"只狼 剑圣","thumbnails":["http://192.168.0.104:8080/image/small/8/24/0","http://192.168.0.104:8080/image/small/8/24/1","http://192.168.0.104:8080/image/small/8/24/2","http://192.168.0.104:8080/image/small/8/24/3","http://192.168.0.104:8080/image/small/8/24/4","http://192.168.0.104:8080/image/small/8/24/5","http://192.168.0.104:8080/image/small/8/24/6","http://192.168.0.104:8080/image/small/8/24/7","http://192.168.0.104:8080/image/small/8/24/8"],"introduction":"九张忍杀截图，帅到爆炸"},{"images":["http://192.168.0.104:8080/image/source/8/23/0","http://192.168.0.104:8080/image/source/8/23/1","http://192.168.0.104:8080/image/source/8/23/2"],"star":0,"author":8,"show":["http://192.168.0.104:8080/image/show/8/23/0","http://192.168.0.104:8080/image/show/8/23/1","http://192.168.0.104:8080/image/show/8/23/2"],"title":"初音未来","number":3,"releasetime":1565097255953,"watch":1,"authorname":"三号测试员","id":23,"keyword":"miku 初音","thumbnails":["http://192.168.0.104:8080/image/small/8/23/0","http://192.168.0.104:8080/image/small/8/23/1","http://192.168.0.104:8080/image/small/8/23/2"],"introduction":"这是初音未来，我最喜欢第一张"}]
     */

    public int code;
    public long time;
    public List<ContentBean> content;

    @Data
    public static class ContentBean implements Parcelable {
        /**
         * images : ["http://192.168.0.104:8080/image/source/8/24/0","http://192.168.0.104:8080/image/source/8/24/1","http://192.168.0.104:8080/image/source/8/24/2","http://192.168.0.104:8080/image/source/8/24/3","http://192.168.0.104:8080/image/source/8/24/4","http://192.168.0.104:8080/image/source/8/24/5","http://192.168.0.104:8080/image/source/8/24/6","http://192.168.0.104:8080/image/source/8/24/7","http://192.168.0.104:8080/image/source/8/24/8"]
         * star : 0
         * author : 8
         * show : ["http://192.168.0.104:8080/image/show/8/24/0","http://192.168.0.104:8080/image/show/8/24/1","http://192.168.0.104:8080/image/show/8/24/2","http://192.168.0.104:8080/image/show/8/24/3","http://192.168.0.104:8080/image/show/8/24/4","http://192.168.0.104:8080/image/show/8/24/5","http://192.168.0.104:8080/image/show/8/24/6","http://192.168.0.104:8080/image/show/8/24/7","http://192.168.0.104:8080/image/show/8/24/8"]
         * title : 只狼忍杀截图
         * number : 9
         * releasetime : 1565098310827
         * watch : 1
         * authorname : 三号测试员
         * id : 24
         * keyword : 只狼 剑圣
         * thumbnails : ["http://192.168.0.104:8080/image/small/8/24/0","http://192.168.0.104:8080/image/small/8/24/1","http://192.168.0.104:8080/image/small/8/24/2","http://192.168.0.104:8080/image/small/8/24/3","http://192.168.0.104:8080/image/small/8/24/4","http://192.168.0.104:8080/image/small/8/24/5","http://192.168.0.104:8080/image/small/8/24/6","http://192.168.0.104:8080/image/small/8/24/7","http://192.168.0.104:8080/image/small/8/24/8"]
         * introduction : 九张忍杀截图，帅到爆炸
         */

        public int star;
        public int author;
        public String title;
        public int number;
        public long releasetime;
        public int watch;
        public String authorname;
        public int id;
        public String keyword;
        public String introduction;
        public List<String> images;
        public List<String> show;
        public List<String> thumbnails;

        protected ContentBean(Parcel in) {
            star = in.readInt();
            author = in.readInt();
            title = in.readString();
            number = in.readInt();
            releasetime = in.readLong();
            watch = in.readInt();
            authorname = in.readString();
            id = in.readInt();
            keyword = in.readString();
            introduction = in.readString();
            images = in.createStringArrayList();
            show = in.createStringArrayList();
            thumbnails = in.createStringArrayList();
        }

        public static final Creator<ContentBean> CREATOR = new Creator<ContentBean>() {
            @Override
            public ContentBean createFromParcel(Parcel in) {
                return new ContentBean(in);
            }

            @Override
            public ContentBean[] newArray(int size) {
                return new ContentBean[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(star);
            parcel.writeInt(author);
            parcel.writeString(title);
            parcel.writeInt(number);
            parcel.writeLong(releasetime);
            parcel.writeInt(watch);
            parcel.writeString(authorname);
            parcel.writeInt(id);
            parcel.writeString(keyword);
            parcel.writeString(introduction);
            parcel.writeStringList(images);
            parcel.writeStringList(show);
            parcel.writeStringList(thumbnails);
        }
    }
}
