#coding=utf-8

import urllib.request
import urllib.parse
from lxml import etree
import sys

def query(content):
    xml_content = '/html/body/text()'
    url = 'https://www.iotroot.com/api/query/search/E=' + urllib.parse.quote(content)
    # 请求头部
    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36 Edg/106.0.1370.37'
    }
    # 利用请求地址和请求头部构造请求对象
    req = urllib.request.Request(url=url, headers=headers, method='GET')
    response = urllib.request.urlopen(req)
    text = response.read().decode('utf-8')
    html = etree.HTML(text)
    content = html.xpath(xml_content)
    return text

if __name__ == '__main__':
    print(query(sys.argv[1]))
