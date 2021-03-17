#coding=utf-8

import urllib.request
import urllib.parse
from lxml import etree
import sys

def query(content):
    xml_content = '//tr/td/text()'
    url = 'https://www.iotroot.com/E=' + urllib.parse.quote(content)
    # 请求头部
    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36'
    }
    # 利用请求地址和请求头部构造请求对象
    req = urllib.request.Request(url=url, headers=headers, method='GET')
    response = urllib.request.urlopen(req)
    text = response.read().decode('utf-8')
    html = etree.HTML(text)
    content = html.xpath(xml_content)
    return content

if __name__ == '__main__':
    print(query(sys.argv[1]))
