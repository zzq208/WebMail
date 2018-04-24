var QMQzoneImg = {
    _mnProjectID: 106,
    _mnPageID: 1,
    _moCaches: {},
    _moSelParam: null,
    _moSelCache: {},
    _mbSupport: true,
    _moPhotoLogic: null
};
QMQzoneImg.getAlbums = function (a) {
    if (!this._mbSupport) {
        return this._callBackCreater("albums", false, a)();
    }
    if (!this._moPhotoLogic) {
        this._initlize();
    }
    if (!a.uin) {
        a.uin = getUin();
    }
    var b = this._getCache(a.uin);
    if (b && b._oAlbums && !a.refresh) {
        return this._callBackCreater("albums", true, a)({albums: b._oAlbums}, true);
    }
    var c = this;
    waitFor(function () {
        return c._moPhotoLogic ? true : false;
    }, function (d) {
        if (!d || !c._moPhotoLogic.getAlbumList) {
            return c._callBackCreater("albums", false, a)();
        }
        c._moPhotoLogic.getAlbumList({
            uin: a.uin,
            callBack: c._callBackCreater("albums", true, a),
            errBack: c._callBackCreater("albums", false, a),
            type: 1,
            refresh: 1,
            "_projectId": c._mnProjectID,
            "_pageId": c._mnPageID
        });
    });
};
QMQzoneImg.getNewPhoto = function (a) {
    if (!this._mbSupport) {
        return this._callBackCreater("newphoto", false, a)();
    }
    if (!this._moPhotoLogic) {
        this._initlize();
    }
    if (!a.uin) {
        a.uin = getUin();
    }
    var b = this._getCache(a.uin);
    if (b && b._oNewPhoto && !a.refresh) {
        return this._callBackCreater("newphoto", true, a)({data: b._oNewPhoto}, true);
    }
    var c = this;
    waitFor(function () {
        return c._moPhotoLogic ? true : false;
    }, function (d) {
        if (!d || !c._moPhotoLogic.getNewPhoto) {
            return c._callBackCreater("newphoto", false, a)();
        }
        c._moPhotoLogic.getNewPhoto({
            uin: a.uin,
            callBack: c._callBackCreater("newphoto", true, a),
            errBack: c._callBackCreater("newphoto", false, a),
            refresh: 1,
            "_projectId": c._mnProjectID,
            "_pageId": c._mnPageID
        });
    });
};
QMQzoneImg.getPhotos = function (a) {
    if (!this._mbSupport) {
        return this._callBackCreater("photos", false, a)();
    }
    if (!this._moPhotoLogic) {
        this._initlize();
    }
    if (!a.uin) {
        a.uin = getUin();
    }
    var b = this._getCache(a.uin);
    if (b && b._oPhotos[a.albumId] && !a.refresh) {
        return this._callBackCreater("photos", true, a)({photos: b._oPhotos[a.albumId]}, true);
    }
    var c = this;
    waitFor(function () {
        return c._moPhotoLogic ? true : false;
    }, function (d) {
        if (!d || !c._moPhotoLogic.getPhotoList) {
            return c._callBackCreater("photos", false, a)();
        }
        c._moPhotoLogic.getPhotoList({
            uin: a.uin || getUin(),
            id: a.albumId,
            refresh: 1,
            callBack: c._callBackCreater("photos", true, a),
            errBack: c._callBackCreater("photos", false, a),
            "_projectId": this._mnProjectID,
            "_pageId": this._mnPageID
        });
    });
};
QMQzoneImg.setPhotoLogic = function (a) {
    this._moPhotoLogic = a;
};
QMQzoneImg.select = function (a) {
    var b = this;
    b._moSelParam = a;
    new QMDialog({
        sTitle: "QQ\u76F8\u518C",
        sBodyHtml: QMQzoneImg._TEMPLATE._BODY.replace({
            images_path: getPath("image"),
            content: QMQzoneImg._TEMPLATE._MSG.replace({msg: QMQzoneImg._TEMPLATE._LOADING.replace({images_path: getPath("image")})})
        }),
        nWidth: 500,
        onload: function () {
            var c = this;
            addEvent(c.S("qzoneimgData"), "click", function (d) {
                var e = d.srcElement || d.target;
                if (e && e.getAttribute("param")) {
                    var f = e.getAttribute("label"), g = e.getAttribute("param");
                    if (a && typeof (a.onclick) == "function") {
                        a.onclick(f, g, b._getCache(getUin())._oPhotos[f][g]);
                    }
                    c.close();
                }
            });
        },
        onshow: function () {
            var d = this, e = d.S("qzoneimgData"), c = b._moSelCache["scrollTop"];
            c && e && (e.scrollTop = c);
            b._initAlbumSelector(d);
        },
        onclose: function () {
            var c = this.S("qzoneimgAlbum"), d = this.S("qzoneimgData");
            if (c) {
                b._moSelCache["label"] = c.getAttribute("label");
            }
            if (d) {
                b._moSelCache["scrollTop"] = d.scrollTop;
            }
        }
    });
};
QMQzoneImg.selectInMenu = function (a) {
    var e = this, f = QMQzoneImg._TEMPLATE_MENU, c = a.oContainer.ownerDocument, g = c.parentWindow || c.defaultView,
        d = {
            oTmpl: f, S: function (h) {
                return S(h, g);
            }
        };
    e._moSelParam = a;
    a.oContainer.innerHTML = f._BODY.replace({
        images_path: getPath("image"),
        content: f._MSG.replace({msg: f._LOADING.replace({images_path: getPath("image")})})
    });
    var b = d.S("qzoneimgData");
    addEvent(b, "click", function (h) {
        var i = h.srcElement || h.target;
        if (i && i.getAttribute("param")) {
            var j = i.getAttribute("label"), k = i.getAttribute("param");
            if (typeof (a.onclick) == "function") {
                a.onclick(j, k, e._getCache(getUin())._oPhotos[j][k]);
            }
        }
    });
    addEvent(b.parentNode, "mousedown", function (h) {
        getTop().hideMenuEvent(h);
    });
    QMQzoneImg._initAlbumSelector(d);
};
QMQzoneImg._initAlbumSelector = function (a) {
    var b = this, c = a.oTmpl || QMQzoneImg._TEMPLATE, d = this._moSelParam.labelIdx || this._moSelCache["label"];
    this.getAlbums({
        onload: function (e, f) {
            var n = a.S("qzoneimgData");
            if (n) {
                if (!e) {
                    return n.innerHTML = c._MSG.replace({msg: "\u76F8\u518C\u5217\u8868\u52A0\u8F7D\u5931\u8D25\uFF01"});
                }
                var m = f;
                if (!m || m.length == 0) {
                    return n.innerHTML = c._MSG.replace({msg: "\u60A8\u8FD8\u6CA1\u521B\u5EFA\u76F8\u518C\u54E6\u3002"});
                }
                var l = a.S("qzoneimgAlbum"), o = [], j = 0;
                for (var p = 0, h = m.length; p < h; p++) {
                    var k = m[p];
                    (!d && k.modifytime > m[j].modifytime || d == k.id) && (j = p);
                    o.push({sId: p, sItemValue: k.name});
                }

                function g(i) {
                    var q = m[i];
                    l.setAttribute("label", q.id);
                    b._romancePhoto(a, q.id);
                }

                new (getTop().QMSelect)({
                    oContainer: l,
                    nWidth: 140,
                    sDefaultId: j,
                    oMenu: {nMaxItemView: 5, nZIndex: 1132, oItems: o},
                    onselect: function (i) {
                        g(i.sId);
                    }
                });
                g(j);
            }
        }
    });
};
QMQzoneImg._romancePhoto = function (a, b) {
    var d = a.S("qzoneimgData");
    _oTmpl = a.oTmpl || QMQzoneImg._TEMPLATE;
    if (!d) {
        return;
    }
    var c = b == this._moSelParam.labelIdx ? this._moSelParam.listIdx : -1;
    d.innerHTML = _oTmpl._MSG.replace({msg: _oTmpl._LOADING.replace({images_path: getPath("image")})});
    this.getPhotos({
        albumId: b, onload: function (e, f) {
            var k = a.S("qzoneimgData");
            if (!k) {
                return;
            }
            else if (!e) {
                k.innerHTML = _oTmpl._MSG.replace({msg: "\u76F8\u7247\u5217\u8868\u52A0\u8F7D\u5931\u8D25\uFF01"});
            }
            else if (!f || f.length == 0) {
                return k.innerHTML = _oTmpl._MSG.replace({msg: "\u8BE5\u76F8\u518C\u6CA1\u6709\u76F8\u7247\u3002"});
            }
            else {
                var j = [];
                for (var l = 0, h = f.length; l < h; l++) {
                    var g = c == l;
                    j.push(_oTmpl._PHOTO.replace({
                        label: b,
                        value: l,
                        url: f[l].pre,
                        thumbclass: g ? "left thumbon pointer attbg bd_upload" : "left thumbnone pointer",
                        thumbnone: "left thumbnone pointer",
                        thumbover: "left thumbover pointer settingtable bd",
                        select: g
                    }));
                }
                k.innerHTML = j.join("");
            }
        }
    });
};
QMQzoneImg._callBackCreater = function (c, a, b) {
    var d = this._getCache(b.uin);
    return function (g, f) {
        if (a && !f) {
            if (c == "albums") {
                d._oAlbums = g[c];
            }
            else if (c == "photos") {
                d._oPhotos[b.albumId] = g[c];
            }
            else if (c == "newphoto") {
                d._oNewPhoto = g.data;
            }
        }
        try {
            if (typeof (b.onload) == "function") {
                if (c != "newphoto") {
                    b.onload(a, a ? g[c] : g, b);
                }
                else {
                    b.onload(a, g.data, b);
                }
            }
        }
        catch (h) {
        }
        if (c == "albums") {
            if (!a) {
                getTop().ossLog("delay", "all", "stat=qzoneimg&type=1&errcode=1");
            }
        }
    };
};
QMQzoneImg._getCache = function (a) {
    return this._moCaches[a] || (this._moCaches[a] = {_oAlbums: null, _oPhotos: {}, _oNewPhoto: null});
};
QMQzoneImg._initlize = function () {
    var b = "qmQzoneImgLogicIframe", a = S(b);
    if (a) {
        if (a.getAttribute("loaded") == "true") {
            this.setPhotoLogic(F(b).PhotoLogic || {});
        }
        return;
    }
    createBlankIframe(window, {
        id: b,
        defcss: false,
        className: "",
        style: "position:absolute;border:none;z-index:99999;right:0;bottom:0;width:1px;height:1px;",
        header: ['<script language="javascript" src="http://qzonestyle.gtimg.cn/ac/qzfl/release/qzfl_for_qzone.js" charset="utf-8"><\/script>', '<script language="javascript" src="http://imgcache.qq.com/qzone/client/photo/pages/qzone_v4/script/photo_logic.js" charset="utf-8"><\/script>'].join(""),
        body: ['<body style="padding:0;margin:0;"></body>'],
        onload: function () {
            var c = this;
            setTimeout(function () {
                c.setAttribute("loaded", "true");
                QMQzoneImg.setPhotoLogic(c.contentWindow.PhotoLogic || {});
            }, 500);
        }
    });
};
QMQzoneImg._TEMPLATE = {};
QMQzoneImg._TEMPLATE._BODY = T(['<div id="qzoneimgAlbum" style="padding:6px 5px 0;"></div>', '<div class="clr"></div>', '<div id="qzoneimgData" class="bd" style="height:270px;*height:290px;margin:4px 5px 5px;overflow-y:auto;padding:10px 10px;" >', '$content$', '</div>', '<div class="dialog_operate addrtitle" style="text-align:left;">(\u70B9\u51FB\u4F7F\u7528\u76F8\u5E94\u7167\u7247)</div>']);
QMQzoneImg._TEMPLATE._PHOTO = T(['<div class="$thumbclass$" label="$label$" param="$value$" select="$select$" ', 'onmouseover="', 'if ( this.getAttribute( \x27select\x27 ) != \x27true\x27 )', '{', 'this.className=\x27$thumbover$\x27;', '}', '" onmouseout="', 'if ( this.getAttribute( \x27select\x27 ) != \x27true\x27 )', '{', 'this.className=\x27$thumbnone$\x27;', '}', '">', '<img class="$thumbimg$" src="$url$" label="$label$" param="$value$" style="width:100px;height:75px;"/>', '</div>']);
QMQzoneImg._TEMPLATE._LOADING = T(['<img src="$images_path$ico_loading21e9c5d.gif" align="absmiddle" />', '&nbsp;&nbsp;\u76F8\u518C\u6570\u636E\u52A0\u8F7D\u4E2D...']);
QMQzoneImg._TEMPLATE._MSG = T(['<table class="addrtitle" style="width:100%;height:100%;"><tr><td align=center>', '$msg$', '</td></tr></table>']);
QMQzoneImg._TEMPLATE_MENU = (function () {
    var a = function () {
    };
    a.prototype = QMQzoneImg._TEMPLATE;
    return new a();
})();
QMQzoneImg._TEMPLATE_MENU._LOADING = T(['<img src="$images_path$ico_loading21e9c5d.gif" align="absmiddle" />', '&nbsp;&nbsp;\u52A0\u8F7D\u4E2D']);
QMQzoneImg._TEMPLATE_MENU._BODY = T(['<div class="QMEditorToolPop" style="width:100%;">', '<h1 class="qmEditorHead">', '<div class="left">QQ\u76F8\u518C</div>', '<div id="qzoneimgAlbum" class="left qmEditorAlbumsSelect"></div>', '<div class="clr"></div></h1>', '<div id="qzoneimgData" class="qzoneimgData">', '$content$', '</div>', '</div>']);
QMQzoneImg._TEMPLATE_MENU._PHOTO = T(['<div class="left qmEditorPicSelect pointer" label="$label$" param="$value$" select="$select$" ', ' onmouseover="this.className=\x27left pointer  qmEditorPicSelect bd\x27;"', ' onmouseout="this.className=\x27left pointer  qmEditorPicSelect\x27;"', '>', '<table cellspacing="0" cellpadding="0"><tbody><tr><td valign="absmiddle">', '<img class="$thumbimg$" src="$url$" label="$label$" param="$value$" style="width:100px;"/>', '</td></tr></tbody></table>', '</div>']);
var qmQzoneImg = QMQzoneImg;
