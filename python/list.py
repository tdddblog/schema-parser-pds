from html.parser import HTMLParser


# -------------------------------------------------------------------

def get_href(attrs):
  for attr in attrs:
    if attr[0] == "href":
      return attr[1]

# -------------------------------------------------------------------

def is_hex_num_char(ch):
  if ch >= '0' and ch <= '9': return True
  if ch >= 'a' and ch <= 'f': return True
  return False

# -------------------------------------------------------------------

def is_hex_number(str):  
  for ch in str.lower():
    if not is_hex_num_char(ch): return False
  return True
    
# -------------------------------------------------------------------

def get_version(name):
  tokens = name.split('.')
  tokens = tokens[0].split('_')
  if len(tokens[-2]) == 4 and is_hex_number(tokens[-2]):
    return tokens[-2] + '_' + tokens[-1]
  return tokens[-1]

# ------------------------------------------------------------

class ListSchemas(HTMLParser):
  def __init__(self, base_url, ns_index):
    super().__init__()
    self.last_ns = ""
    self.base_url = base_url
    self.ns_index = ns_index


  def handle_starttag(self, tag, attrs):
    if tag != "a": 
      return

    href = get_href(attrs)
    if href and href.lower().endswith(".json"):
      tokens = href.split('/')
      ns = tokens[self.ns_index]

      # Only process first entry
      if self.last_ns == ns: 
        return

      self.last_ns = ns
      name = tokens[-1]
      print('"' + ns + '","' + self.base_url + href + '","' + get_version(name) + '"')


# ------------------------------------------------------------

def parse_pds_dics():
# https://pds.nasa.gov/datastandards/dictionaries/index-1.15.0.0.shtml
  ns_index = 2
  f = open("c:\\tmp\\schema\\index-1.15.0.0.shtml", "r", encoding="utf-8")
  data = f.read()
  f.close()

  parser = ListSchemas("https://pds.nasa.gov", ns_index)
  parser.feed(data)
  parser.close()

# ------------------------------------------------------------

def parse_missions_dics():
# https://pds.nasa.gov/datastandards/dictionaries/index-missions.shtml
  ns_index = 3
  f = open("c:\\tmp\\schema\\Missions.shtml", "r", encoding="utf-8")
  data = f.read()
  f.close()

  parser = ListSchemas("https://pds.nasa.gov", ns_index)
  parser.feed(data)
  parser.close()

# ------------------------------------------------------------

parse_missions_dics()
