/**
 * Created by DD on 6/8/2015.
 */
var tools;  // global variable
$(function () {
    "use strict";
    var el = {
        $jsLoading: $('.js-loading'),

        $jsConfirmTipModel: $('#confirm-tip-model'),
        $jsModelTitle: $('#confirm-tip-model .js-model-title'),
        $jsImgFail: $('#confirm-tip-model .js-img-fail'),
        $jsImgSuccess: $('#confirm-tip-model .js-img-success'),
        $jsImgWarning: $('#confirm-tip-model .js-img-warning'),
        $jsModelText: $('#confirm-tip-model .js-model-text'),
        $jsConfirmModelLeftBtn: $('#confirm-tip-model .js-confirm-model-left-btn'),
        $jsConfirmModelRightBtn: $('#confirm-tip-model .js-confirm-model-right-btn')
    };

    tools = {
        message: {
            validFail: {
                title: '注意',
                text: '请按照页面中的提示填写信息...',
                type: 'warning'
            }
        },
        /**
         * open loading
         */
        openLoading: function () {
            el.$jsLoading.removeClass('hidden');
        },
        /**
         * close loading
         */
        closeLoading: function () {
            el.$jsLoading.addClass('hidden');
        },
        /**
         * open simple tip
         * http://boedesign.com/blog/2009/07/11/growl-for-jquery-gritter
         * {type: 'fail | success | warning'}
         * 参考 Gritter 使用 http://boedesign.com/blog/2009/07/11/growl-for-jquery-gritter
         * 增加了 一个 type 属性； 设置 type 就不需要设置 image 属性； type 可以设置 三个值 fail, success, waring
         * 其他使用与 Gritter param， 其实常用的只有 text， title ， type 这三个属性
         */
        openST: function (param) {
            if (!param) {
                param = {};
            }
            var type = {
                fail: contentPath + '/img/tip-fail1.png',
                success: contentPath + '/img/tip-success1.png',
                warning: contentPath + '/img/tip-warning1.png'
            };
            param.image = type[param.type] ? type[param.type] : type.success;
            var className = type[param.type] ? 'gritter-' + type[param.type] : 'gritter-' + type.success;
            var data = {
                // (string | mandatory) the heading of the notification
                title: '我想你忘记设置提示标题了',
                // (string | mandatory) the text inside the notification
                text: '我想你忘记设置提示信息了',
                // (string | optional) the image to display on the left
                image: contentPath + '/img/tip-success.png',
                // (bool | optional) if you want it to fade out on its own or just sit there
                sticky: false,
                // (int | optional) the time you want it to be alive for before fading out (milliseconds)
                time: 3000,
                // (string | optional) the class name you want to apply directly to the notification for custom styling
                class_name: ''
            };
            data = $.extend(data, param);
            $.gritter.add(data);
        },
        /**
         * close simple tip
         * close all gritter
         */
        closeST: function () {
            $.gritter.removeAll();
        },
        /**
         * open model tip
         * @param data {Object}
         * something like this:
          tools.openCT({
                title: 'hello',         // {String} required model title
                text: '成功了！！！',   // {String} required model text
                type: 'success',        // {String} required 取值 success， fail， warning， default success
                buttons: [              // {Array} required buttons, 可以有一个 button
                    {
                        text: '返回上一页 isClose=true',     // {String} required button text
                        fn: function () {                   // {Function} click function
                            alert(1); // you code here
                        },
                        isClose: true           // {Boolean} 执行完代码后是否关闭 model
                    },
                    {
                        text: '首页 isClose=false',
                        fn: function () {
                            alert(2); // you code here
                        },
                        isClose: false
                    }
                ]
            });
         */
        openCT: function (data) {
            el.$jsModelTitle.text(data.title);
            el.$jsModelText.text(data.text);

            if (data.type === 'fail') {
                el.$jsImgFail.removeClass('hidden');
            } else if (data.type === 'success') {
                el.$jsImgSuccess.removeClass('hidden');
            } else if (data.type === 'warning') {
                el.$jsImgWarning.removeClass('hidden');
            } else {
                el.$jsImgSuccess.removeClass('hidden');
            }

            var buttonLen = data.buttons.length;

            if (buttonLen === 1 ) {
                el.$jsConfirmModelLeftBtn.text(data.buttons[0].text);
                el.$jsConfirmModelLeftBtn.removeClass('hidden').on('click', function () {
                    data.buttons[0].fn && data.buttons[0].fn(this);
                    if (data.buttons[0].isClose === false) {
                        return;
                    }
                    tools.closeCT();
                });
            }

            if (buttonLen === 2) {
                el.$jsConfirmModelLeftBtn.text(data.buttons[0].text);
                el.$jsConfirmModelLeftBtn.removeClass('hidden').on('click', function () {
                    data.buttons[0].fn && data.buttons[0].fn(this);
                    if (data.buttons[0].isClose === false) {
                        return;
                    }
                    tools.closeCT();
                });

                el.$jsConfirmModelRightBtn.text(data.buttons[1].text);
                el.$jsConfirmModelRightBtn.removeClass('hidden').on('click', function () {
                    data.buttons[1].fn && data.buttons[1].fn(this);
                    if (data.buttons[1].isClose === false) {
                        return;
                    }
                    tools.closeCT();
                });
            }


            el.$jsConfirmTipModel.modal('show');
        },
        /**
         * close model tip
         */
        closeCT: function () {
            el.$jsConfirmTipModel.modal('hide');
        },
        /**
         * reset model tip
         */
        defaultST: function () {
            el.$jsConfirmTipModel.on('hidden.bs.modal', function () {
                el.$jsImgFail.addClass('hidden');
                el.$jsImgSuccess.addClass('hidden');
                el.$jsImgWarning.addClass('hidden');
                el.$jsConfirmModelLeftBtn.addClass('hidden').off('click');
                el.$jsConfirmModelRightBtn.addClass('hidden').off('click');
                $('body').addClass('modal-open');
            });
        },
        init: function () {
            this.defaultST();

        }
    };
    tools.init();

});