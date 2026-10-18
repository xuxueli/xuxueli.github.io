#!/usr/bin/env python3
"""Generate 1200x630 OG image for xuxueli.github.io (XXL Open Source Community).

Portable Pillow-only generator. Uses the site's exact brand tokens:
  - teal accent #65BE93 / #26A69A (favicon + materialize teal)
  - dark text   #444444
  - body bg     #FFFFFF
CJK rendered with WenQuanYi Zen Hei (compatible with Chinese layout).
The XXL logo mark is pasted from the repo copy when available, otherwise
drawn as a teal rounded square with the XXL wordmark.
"""
from PIL import Image, ImageDraw, ImageFont
import os

W, H = 1200, 630
BG = (255, 255, 255)
TEAL = (38, 166, 154)      # #26a69a materialize teal
TEAL_DARK = (37, 130, 118) # darkened teal for depth
GREEN = (101, 190, 147)    # #65BE93 favicon mark
TEXT = (68, 68, 68)        # #444444
MUTED = (120, 130, 140)
SOFT = (245, 250, 249)     # faint teal tint panel


def font(path, size):
    return ImageFont.truetype(path, size)


F_BOLD = "/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf"
F_REG = "/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf"
CJK = "/usr/share/fonts/truetype/wqy/wqy-zenhei.ttc"

img = Image.new("RGB", (W, H), BG)
d = ImageDraw.Draw(img)

# top teal band
d.rectangle([0, 0, W, 260], fill=TEAL)
# subtle darker bottom edge on band
d.rectangle([0, 244, W, 260], fill=TEAL_DARK)

# logo mark (paste repo copy, else draw placeholder)
logo_path = os.path.join(os.path.dirname(os.path.abspath(__file__)),
                         "..", "project", "static", "xxl-job", "images", "xxl-logo.png")
logo_path = os.path.normpath(logo_path)
cx, cy = 170, 130
if os.path.exists(logo_path):
    logo = Image.open(logo_path).convert("RGBA")
    logo = logo.resize((180, 180), Image.LANCZOS)
    img.paste(logo, (cx - 90, cy - 90), logo)
else:
    d.rounded_rectangle([cx - 90, cy - 90, cx + 90, cy + 90], radius=24, fill=(255, 255, 255))
    d.text((cx, cy), "XXL", font=font(F_BOLD, 72), fill=TEAL, anchor="mm")

# wordmark (bilingual)
f_brand = font(F_BOLD, 78)
d.text((300, cy - 40), "XXL Open Source", font=f_brand, fill=(255, 255, 255), anchor="lm")
f_cjk = font(CJK, 52)
d.text((302, cy + 52), "XXL开源社区", font=f_cjk, fill=(220, 255, 248), anchor="lm")

# body title
f_h1 = font(F_BOLD, 50)
d.text((600, 372), "Free & open-source community projects", font=f_h1, fill=TEXT, anchor="mm")

# chips of flagship projects
chips = ["XXL-JOB", "XXL-RPC", "XXL-SSO", "XXL-MQ", "XXL-API", "XXL-CACHE"]
chip_w, chip_h = 178, 64
gap = 16
total = len(chips) * chip_w + (len(chips) - 1) * gap
x0 = 600 - total // 2
y0 = 430
f_chip = font(F_BOLD, 28)
for label in chips:
    d.rounded_rectangle([x0, y0, x0 + chip_w, y0 + chip_h], radius=16, fill=SOFT, outline=GREEN, width=3)
    d.text((x0 + chip_w // 2, y0 + chip_h // 2), label, font=f_chip, fill=TEAL_DARK, anchor="mm")
    x0 += chip_w + gap

# tagline
f_tag = font(F_REG, 30)
d.text((600, 536), "Founded 2015 · Open, free and shared", font=f_tag, fill=MUTED, anchor="mm")

# domain pill
pill_w, pill_h = 400, 54
px = 600 - pill_w // 2
py = 560
d.rounded_rectangle([px, py, px + pill_w, py + pill_h], radius=27, fill=BG, outline=TEAL, width=3)
d.text((600, py + pill_h // 2), "www.xuxueli.com", font=font(F_BOLD, 26), fill=TEXT, anchor="mm")

img.save("og-image.png")
print("saved og-image.png", img.size)